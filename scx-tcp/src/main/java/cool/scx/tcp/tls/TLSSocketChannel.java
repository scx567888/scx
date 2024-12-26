package cool.scx.tcp.tls;

import cool.scx.io.BufferedByteChannelDataSupplier;
import cool.scx.io.DataNode;
import cool.scx.io.PowerfulLinkedDataReader;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

//todo 未完成
public class TLSSocketChannel extends AbstractSocketChannel {

    private final SSLEngine sslEngine;

    private final ByteBuffer inboundAppData;// 存储已经解密的数据 (相当于缓存)
    private final ByteBuffer outboundNetData;// 存储将要发送到远端的加密数据
    private final ByteBuffer inboundNetData;// 存储从远端读取到的加密数据
    private final PowerfulLinkedDataReader rawReader;
    private final PowerfulLinkedDataReader dataReader;
    private final int packetBufferSize;
    private final int applicationBufferSize;

    public TLSSocketChannel(SocketChannel socketChannel, SSLEngine sslEngine) {
        super(socketChannel);
        this.sslEngine = sslEngine;

        var session = sslEngine.getSession();
        this.inboundAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.outboundNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        this.inboundNetData = ByteBuffer.allocate(session.getPacketBufferSize());

        this.applicationBufferSize = session.getApplicationBufferSize();
        this.packetBufferSize = session.getPacketBufferSize();

        this.rawReader = new PowerfulLinkedDataReader(new BufferedByteChannelDataSupplier(socketChannel, session.getPacketBufferSize()));

        this.dataReader = new PowerfulLinkedDataReader(this::decodeDataSupplier);
    }

    public static byte[] readBytes(ByteBuffer buffer) {
        var bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

    public static ByteBuffer expandBuffer(ByteBuffer oldBuffer) {
        return expandBuffer(oldBuffer, oldBuffer.capacity() * 2);
    }

    public static ByteBuffer expandBuffer(ByteBuffer oldBuffer, int size) {
        var newBuffer = ByteBuffer.allocate(size);
        oldBuffer.flip();
        newBuffer.put(oldBuffer);
        return newBuffer;
    }

    /**
     * 会自动扩容
     *
     * @param buffer b
     * @param bytes  b
     * @return b
     */
    public static ByteBuffer putBytes(ByteBuffer buffer, byte[] bytes) {
        if (buffer.remaining() < bytes.length) {
            int newCapacity = Math.max(buffer.capacity() * 2, buffer.capacity() + bytes.length);
            buffer = expandBuffer(buffer, newCapacity);
        }
        buffer.put(bytes);
        return buffer;
    }

    public DataNode decodeDataSupplier() {
        inboundAppData.clear();
        try {
            //读取加密后的数据
            var encryptedBuffer = ByteBuffer.wrap(rawReader.fastPeek(packetBufferSize));

            // 解密后的数据
            var decryptedBuffer = inboundAppData;

            var totalBytesConsumed = 0;
            var totalBytesProduced = 0;

            //我们这里尝试将读取到的数据全部解密
            var i = 0;
            _MAIN:
            while (encryptedBuffer.hasRemaining()) {

                var result = sslEngine.unwrap(encryptedBuffer, decryptedBuffer);

                totalBytesConsumed += result.bytesConsumed();
                totalBytesProduced += result.bytesProduced();

                switch (result.getStatus()) {
                    case OK -> {
                        // 解密成功，无需额外操作
                    }
                    case BUFFER_OVERFLOW -> {
                        // decryptedBuffer 容量不足, 进行扩容 2 倍,
                        // 这里无需进行原有数据的复制 因为 BUFFER_OVERFLOW 的时候 decryptedBuffer 是不会被填充数据的
                        if (totalBytesProduced > 0) {
                            break _MAIN;
                        } else {
                            //todo 这里需要处理 
                            System.out.println("扩容了");
                            decryptedBuffer = ByteBuffer.allocate(decryptedBuffer.capacity() * 2);
                        }
                    }
                    case BUFFER_UNDERFLOW -> {
                        // 这里表示 待解密数据不足 但这里有可能已经 解密成功了一些数据
                        // 1, 如果已经解密了一些数据 我们跳过这次的扩容
                        // 2, 如果之前没有解密任何数据 我们需要扩容并重新读取数据
                        if (totalBytesProduced > 0) {
                            break _MAIN;
                        } else {

                            //todo 这里需要处理
                            System.out.println("需要扩容了");
                        }
                    }
                    case CLOSED -> {
                        //通道关闭 表示我们读取不到任何数据了
                        return null;
                    }
                }
            }

            //此处我们跳过值
            rawReader.skip(totalBytesConsumed);

            // 切换到读模式
            decryptedBuffer.flip();

            //返回
            return new DataNode(decryptedBuffer.array(), decryptedBuffer.position(), decryptedBuffer.limit());

        } catch (SSLException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void startHandshake() throws IOException {
        sslEngine.beginHandshake();

        //这里我们可能会出现需要扩容的情况(当然概率很低) 所以 复制一份缓冲区
        var outboundNetData = this.outboundNetData;
        var inboundNetData = this.inboundNetData;

        _MAIN:
        while (true) {
            var handshakeStatus = sslEngine.getHandshakeStatus();
            switch (handshakeStatus) {
                case NEED_UNWRAP -> {
                    // 这里不停循环 直到完成
                    _NU:
                    while (true) {
                        //读取远程数据到入站网络缓冲区
                        if (socketChannel.read(inboundNetData) == -1) {
                            throw new SSLHandshakeException("Channel closed during handshake");
                        }
                        //切换成读模式
                        inboundNetData.flip();
                        //使用空缓冲区接收 因为握手阶段是不会有任何数据的
                        var result = sslEngine.unwrap(inboundNetData, ByteBuffer.allocate(0));
                        switch (result.getStatus()) {
                            case OK -> {
                                // 解密成功，直接继续进行，因为握手阶段 即使 unwrap , 解密数据容量也只会是空 所以跳过处理
                                break _NU;
                            }
                            case BUFFER_OVERFLOW -> {
                                // 解密后数据缓冲区 容量太小 无法容纳解密后的数据
                                // 但在握手阶段 和 OK 分支同理 理论上不会发生 所以这里我们抛出错误
                                throw new SSLHandshakeException("Unexpected buffer overflow");
                            }
                            case BUFFER_UNDERFLOW -> {
                                // inboundNetData 数据不够解密 需要继续获取 这里有两种情况
                                int remainingSpace = inboundNetData.capacity() - inboundNetData.limit();
                                // 1, inboundNetData 本身容量太小 我们需要扩容
                                if (remainingSpace == 0) {
                                    var newInboundNetData = ByteBuffer.allocate(inboundNetData.capacity() * 2);
                                    //把原有数据放进去
                                    newInboundNetData.put(inboundNetData);
                                    inboundNetData = newInboundNetData;
                                }
                                // 2, 远端未发送完整的数据 我们需要继续读取数据 (这种情况很少见) 这里直接继续循环
                            }
                            case CLOSED -> {
                                throw new SSLHandshakeException("closed on handshake wrap");
                            }
                        }
                    }
                    //压缩数据
                    inboundNetData.compact();
                }
                case NEED_WRAP -> {
                    // 清空出站网络缓冲区
                    outboundNetData.clear();
                    // 这里不停循环 直到完成
                    _NW:
                    while (true) {
                        // 加密一个空的数据 这里因为待加密数据是空的 所以不需要循环去加密 单次执行即可
                        var result = sslEngine.wrap(ByteBuffer.allocate(0), outboundNetData);
                        switch (result.getStatus()) {
                            case OK -> {
                                //切换到读模式
                                outboundNetData.flip();
                                //循环发送到远端
                                while (outboundNetData.hasRemaining()) {
                                    socketChannel.write(outboundNetData);
                                }
                                break _NW;
                            }
                            case BUFFER_OVERFLOW -> {
                                // outboundNetData 容量太小 无法容纳加密后的数据 需要扩容 outboundNetData, 这里采取 2 倍
                                // 但在实际环境中 默认的 outboundNetData 大小是 session.getPacketBufferSize()
                                // 所以几乎不会出现这种情况 但这里还是做一个处理
                                outboundNetData = ByteBuffer.allocate(outboundNetData.capacity() * 2);
                            }
                            case BUFFER_UNDERFLOW -> {
                                // 待加密数据 数据量不足, 在 wrap 中理论上不会发生 所以这里我们抛出错误
                                throw new SSLHandshakeException("buffer underflow on handshake wrap");
                            }
                            case CLOSED -> {
                                throw new SSLHandshakeException("closed on handshake wrap");
                            }
                        }
                    }
                }
                case NEED_TASK -> {
                    // 处理委派任务 这里我们直接在当前线程中运行
                    while (true) {
                        var task = sslEngine.getDelegatedTask();
                        if (task == null) {
                            break;
                        }
                        task.run();
                    }
                }
                case FINISHED -> {
                    // 握手完成 退出主循环
                    break _MAIN;
                }
                case NOT_HANDSHAKING -> {
                    // 当前不在进行握手操作，退出主循环
                    break _MAIN;
                }
            }
        }
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return dataReader.tryRead(dst);
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        int n = 0;

        //可能出现扩容的情况 这里使用单独的
        var outboundNetData = this.outboundNetData;

        while (src.hasRemaining()) {
            //重置
            outboundNetData.clear();

            //加密
            var result = sslEngine.wrap(src, outboundNetData);

            switch (result.getStatus()) {
                case OK -> {
                    //切换到读模式
                    outboundNetData.flip();

                    //循环发送
                    while (outboundNetData.hasRemaining()) {
                        socketChannel.write(outboundNetData);
                    }
                    n += result.bytesConsumed();
                }
                case BUFFER_OVERFLOW -> {
                    // 直接扩容即可 以 2 倍为基准
                    outboundNetData = ByteBuffer.allocate(outboundNetData.capacity() * 2);
                }
                case BUFFER_UNDERFLOW -> {
                    throw new IOException("SSLEngine wrap 遇到 BUFFER_UNDERFLOW, 这不应该发生 !!!");
                }
                case CLOSED -> {
                    throw new IOException("SSLEngine wrap 遇到 CLOSED");
                }
            }
        }

        return n;
    }

}

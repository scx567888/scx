package cool.scx.tcp;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioTLSTCPSocket implements ScxTCPSocket {

    private final SocketChannel socketChannel;
    private final SSLEngine sslEngine;

    private final ByteBuffer outboundAppData;
    private final ByteBuffer outboundNetData;
    private final ByteBuffer inboundAppData;
    private final ByteBuffer inboundNetData;

    private final InputStream inputStream;
    private final OutputStream outputStream;

    public NioTLSTCPSocket(SocketChannel socketChannel, SSLEngine sslEngine) {
        this.socketChannel = socketChannel;
        this.sslEngine = sslEngine;

        var session = sslEngine.getSession();
        this.outboundAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.outboundNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        this.inboundAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.inboundNetData = ByteBuffer.allocate(session.getPacketBufferSize());

        this.inputStream = createInputStream();
        this.outputStream = createOutputStream();
    }

    private InputStream createInputStream() {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                ByteBuffer buffer = ByteBuffer.allocate(1);
                return NioTLSTCPSocket.this.read(buffer) > 0 ? buffer.get() & 0xFF : -1;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                ByteBuffer buffer = ByteBuffer.wrap(b, off, len);
                return NioTLSTCPSocket.this.read(buffer);
            }
        };
    }

    private OutputStream createOutputStream() {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                ByteBuffer buffer = ByteBuffer.allocate(1);
                buffer.put((byte) b);
                buffer.flip();
                NioTLSTCPSocket.this.write(buffer);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                ByteBuffer buffer = ByteBuffer.wrap(b, off, len);
                NioTLSTCPSocket.this.write(buffer);
            }
        };
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

    public int read(ByteBuffer dst) throws IOException {
        return -1;
    }

    public void write(ByteBuffer buffer) throws IOException {
        //可能出现扩容的情况 这里使用单独的
        var outboundNetData = this.outboundNetData;

        //重置
        outboundNetData.clear();

        while (buffer.hasRemaining()) {
            //加密
            var result = sslEngine.wrap(buffer, outboundNetData);

            switch (result.getStatus()) {
                case OK -> {
                    //切换到读模式
                    outboundNetData.flip();

                    //循环发送
                    while (outboundNetData.hasRemaining()) {
                        socketChannel.write(outboundNetData);
                    }
                }
                case BUFFER_OVERFLOW -> {
                    // 直接扩容即可 以 2 倍为基准
                    outboundNetData = ByteBuffer.allocate(outboundNetData.capacity() * 2);
                }
                // 不应该发生在 wrap 操作中  
                case BUFFER_UNDERFLOW -> throw new IOException("SSLEngine wrap 遇到 BUFFER_UNDERFLOW");
                // 不应该发生在 wrap 操作中
                case CLOSED -> throw new IOException("SSLEngine wrap 遇到 CLOSED");
            }
        }
    }

    @Override
    public InputStream inputStream() {
        return inputStream;
    }

    @Override
    public OutputStream outputStream() {
        return outputStream;
    }

    @Override
    public boolean isClosed() {
        return !socketChannel.isOpen();
    }

    @Override
    public SocketAddress remoteAddress() throws IOException {
        return socketChannel.getRemoteAddress();
    }

    @Override
    public void close() throws IOException {
        sslEngine.closeOutbound();
        socketChannel.close();
    }

}

package cool.scx.tcp.tls;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static cool.scx.io.IOHelper.transferByteBuffer;

/**
 * TLSSocketChannel
 *
 * @author scx567888
 * @version 0.0.1
 */
public class TLSSocketChannel extends AbstractSocketChannel {

    private final SSLEngine sslEngine;

    // 存储应用数据 (已加密) 这里不使用其缓存任何数据 仅仅是为了减少频繁创建 ByteBuffer 造成的性能损失
    // 这里不是 final 的是因为本身可能会进行扩容
    private ByteBuffer outboundNetData;

    // 存储网络数据 (未解密) 同时会缓存 tcp 半包 
    // 这里不是 final 的是因为本身需要用来存储 半包数据
    private ByteBuffer inboundNetData;

    // 存储应用数据 (已解密) 这里不使用其缓存任何数据 仅仅是为了减少频繁创建 ByteBuffer 造成的性能损失
    // 这里不是 final 的是因为本身可能会进行扩容
    private ByteBuffer inboundAppData;

    public TLSSocketChannel(SocketChannel socketChannel, SSLEngine sslEngine) {
        super(socketChannel);
        this.sslEngine = sslEngine;
        //这里默认容量采用 SSLSession 提供的 以便减小扩容的概率
        var session = sslEngine.getSession();
        var packetBufferSize = session.getPacketBufferSize();
        var applicationBufferSize = session.getApplicationBufferSize();
        this.outboundNetData = ByteBuffer.allocate(1); //永远都是写模式
        this.inboundNetData = ByteBuffer.allocate(packetBufferSize); //永远都是写模式
        this.inboundAppData = ByteBuffer.allocate(applicationBufferSize).flip(); //永远都是读模式
    }

    public void startHandshake() throws IOException {
        sslEngine.beginHandshake();

        _MAIN:
        while (true) {
            var handshakeStatus = sslEngine.getHandshakeStatus();
            switch (handshakeStatus) {
                case NEED_UNWRAP -> {
                    // 这里不停循环 直到完成
                    _NU:
                    while (true) {
                        //缓冲区没有数据我们才读取
                        if (inboundNetData.position() == 0) {
                            //读取远程数据到入站网络缓冲区
                            if (socketChannel.read(inboundNetData) == -1) {
                                throw new SSLHandshakeException("Channel closed during handshake");
                            }
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
        //0, 如果有剩余则先使用剩余的
        if (inboundAppData.hasRemaining()) {
            return transferByteBuffer(inboundAppData, dst);
        }

        //1, 重置缓冲区以进行新的读取操作
        inboundAppData.clear();

        //计算本次 get 总的 解密和加密数据量, 用于判断当遇见 TCP 半包时是直接终止还是继续 read 
        var totalBytesConsumed = 0;
        var totalBytesProduced = 0;

        //这里涉及到如果遇到 tcp 半包 我们需要尝试重新读取 (如果第一次就遇到了) 所以这里采用一个 while 循环
        _R:
        while (true) {

            //尝试读取
            var bytesRead = socketChannel.read(inboundNetData);

            //没有数据我们返回 null, 就如同 DataSupplier 规定的
            if (bytesRead == -1) {
                return -1;
            }

            //转换为读模式 准备用于解密
            inboundNetData.flip();

            //这里我们尝试解密所有读取到的数据 直到剩余解密数据为空或者遇到 TCP 半包
            _UW:
            while (inboundNetData.hasRemaining()) {

                var result = sslEngine.unwrap(inboundNetData, inboundAppData);

                //更新 字节消费与生产数量
                totalBytesConsumed += result.bytesConsumed();
                totalBytesProduced += result.bytesProduced();

                switch (result.getStatus()) {
                    case OK -> {
                        // 解密成功，无需额外操作 继续解密即可
                    }
                    case BUFFER_OVERFLOW -> {
                        // appBuffer 容量不足, 这里进行扩容 2 倍
                        // 原有的已经解密的数据别忘了放进去
                        var newAppBuffer = ByteBuffer.allocate(inboundAppData.capacity() * 2);
                        newAppBuffer.put(inboundAppData.flip()); // 这里 appBuffer 是写状态 所以需要翻转一下
                        inboundAppData = newAppBuffer;
                    }
                    case BUFFER_UNDERFLOW -> {
                        // 这里表示 netBuffer 中待解密数据不足 这里分为两种情况
                        // 1, 如果已经成功解密了部分数据 我们跳过这次的扩容
                        if (totalBytesProduced > 0) {
                            break _UW;
                        }
                        // 2, 如果之前没有解密成功任何数据 我们需要扩容 netBuffer 并重新从网络中读取数据
                        // 原有的已经读取的数据别忘了放进去
                        var newNetBuffer = ByteBuffer.allocate(inboundNetData.capacity() * 2);
                        newNetBuffer.put(inboundNetData);// 这里 netBuffer 已经是读状态 不需要 flip()
                        inboundNetData = newNetBuffer;
                        continue _R;
                    }
                    case CLOSED -> {
                        //通道关闭 但是我们有可能之前已经读取到了部分数据这里需要 跳出循环以便返回剩余数据
                        break _R;
                    }
                }
            }

            //退出主循环
            break;
        }

        //这里我们的 netBuffer 中可能有一些残留数据 我们压缩一下 以便下次继续使用
        inboundNetData.compact();

        // 将 appBuffer 切换到读模式并转换为 DataNode
        inboundAppData.flip();

        //返回
        return transferByteBuffer(inboundAppData, dst);
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        int n = 0;

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
                    // 直接扩容即可 以 2 倍为基准 (这种情况很少发生)
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

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        //todo 代码待抽取
        // 发起关闭握手
        sslEngine.closeOutbound();

        // 创建一个空缓冲区用于发送关闭消息
        ByteBuffer emptyBuffer = ByteBuffer.allocate(0);
        // 缓冲区用于存储出站数据
        ByteBuffer outboundBuffer = outboundNetData;

        // 完成关闭握手
        while (!sslEngine.isOutboundDone()) {
            outboundBuffer.clear();
            SSLEngineResult result = sslEngine.wrap(emptyBuffer, outboundBuffer);
            switch (result.getStatus()) {
                case OK -> {
                    outboundBuffer.flip();
                    while (outboundBuffer.hasRemaining()) {
                        socketChannel.write(outboundBuffer);
                    }
                }
                case CLOSED -> {
                }
                default -> throw new IOException("Error during SSL/TLS close handshake: " + result.getStatus());
            }
        }

        // 接收对方的 CLOSE_NOTIFY 消息
        var emptyReadBuffer = ByteBuffer.allocate(0);
        var b = true;
        while (b) {
            b = sslEngine.isInboundDone();
            read(emptyReadBuffer);
        }
        // 关闭 SocketChannel
        socketChannel.close();
    }

}

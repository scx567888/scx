package cool.scx.tcp.tls;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TLSHandshakeHelper {

    public static void startHandshake(SocketChannel socketChannel, SSLEngine sslEngine) throws IOException {
        sslEngine.beginHandshake();

        //创建出站和入站缓冲区
        var outboundNetData = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
        var inboundNetData = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());

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


}

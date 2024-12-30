package cool.scx.tcp.tls;

import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static cool.scx.io.IOHelper.transferByteBuffer;

//todo 未完成
public class TLSSocketChannel extends AbstractSocketChannel {

    private final SSLEngine sslEngine;

    // 存储应用数据 (已加密) 这里不使用其缓存任何数据 仅仅是为了减少频繁创建 ByteBuffer 造成的性能损失
    private final ByteBuffer outboundNetData;

    // 存储网络数据 (未解密) 同时会缓存 tcp 半包 
    // 这里不是 final 的是因为本身需要用来存储 半包数据
    private ByteBuffer appBuffer;

    // 存储应用数据 (已解密) 这里不使用其缓存任何数据 仅仅是为了减少频繁创建 ByteBuffer 造成的性能损失
    // 这里不是 final 的是因为本身可能会进行扩容
    private ByteBuffer netBuffer;

    public TLSSocketChannel(SocketChannel socketChannel, SSLEngine sslEngine) {
        super(socketChannel);
        this.sslEngine = sslEngine;
        //这里默认容量采用 SSLSession 提供的 以便减小扩容的概率
        this.outboundNetData = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
        this.netBuffer = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
        this.appBuffer = ByteBuffer.allocate(sslEngine.getSession().getApplicationBufferSize());
        this.appBuffer.flip();
    }

    public void startHandshake() throws IOException {
        TLSHandshakeHelper.startHandshake(socketChannel, sslEngine);
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        //0, 如果有剩余则先使用剩余的
        if (appBuffer.hasRemaining()) {
            return transferByteBuffer(appBuffer, dst);
        }

        //1, 重置缓冲区以进行新的读取操作
        appBuffer.clear();

        //计算本次 get 总的 解密和加密数据量, 用于判断当遇见 TCP 半包时是直接终止还是继续 read 
        var totalBytesConsumed = 0;
        var totalBytesProduced = 0;

        //这里涉及到如果遇到 tcp 半包 我们需要尝试重新读取 (如果第一次就遇到了) 所以这里采用一个 while 循环
        _R:
        while (true) {

            //尝试读取
            var bytesRead = socketChannel.read(netBuffer);

            //没有数据我们返回 null, 就如同 DataSupplier 规定的
            if (bytesRead == -1) {
                return -1;
            }

            //转换为读模式 准备用于解密
            netBuffer.flip();

            //这里我们尝试解密所有读取到的数据 直到剩余解密数据为空或者遇到 TCP 半包
            _UW:
            while (netBuffer.hasRemaining()) {

                var result = sslEngine.unwrap(netBuffer, appBuffer);

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
                        var newAppBuffer = ByteBuffer.allocate(appBuffer.capacity() * 2);
                        newAppBuffer.put(appBuffer.flip()); // 这里 appBuffer 是写状态 所以需要翻转一下
                        appBuffer = newAppBuffer;
                    }
                    case BUFFER_UNDERFLOW -> {
                        // 这里表示 netBuffer 中待解密数据不足 这里分为两种情况
                        // 1, 如果已经成功解密了部分数据 我们跳过这次的扩容
                        if (totalBytesProduced > 0) {
                            break _UW;
                        }
                        // 2, 如果之前没有解密成功任何数据 我们需要扩容 netBuffer 并重新从网络中读取数据
                        // 原有的已经读取的数据别忘了放进去
                        var newNetBuffer = ByteBuffer.allocate(netBuffer.capacity() * 2);
                        newNetBuffer.put(netBuffer);// 这里 netBuffer 已经是读状态 不需要 flip()
                        netBuffer = newNetBuffer;
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
        netBuffer.compact();

        // 将 appBuffer 切换到读模式并转换为 DataNode
        appBuffer.flip();

        //返回
        return transferByteBuffer(appBuffer, dst);
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

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        //todo ? 此处是否有问题 ?
        sslEngine.closeOutbound();
        socketChannel.close();
    }

}

package cool.scx.tcp.tls;

import cool.scx.io.OutputStreamDataConsumer;
import cool.scx.io.PowerfulLinkedDataReader;

import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

//todo 未完成
public class TLSSocketChannel extends AbstractSocketChannel {

    private final SSLEngine sslEngine;

    // 存储应用数据 (已加密) 这里不使用其缓存任何数据 仅仅是为了减少频繁创建 ByteBuffer 造成的性能损失
    private final ByteBuffer outboundNetData;

    //解密数据 读取器
    private final PowerfulLinkedDataReader dataReader;

    //针对需要获取 InputStream 的情况提供一个 性能优化的 InputStream 实例
    private InputStream in;

    public TLSSocketChannel(SocketChannel socketChannel, SSLEngine sslEngine) {
        super(socketChannel);
        this.sslEngine = sslEngine;
        this.outboundNetData = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
        this.dataReader = new PowerfulLinkedDataReader(new TLSDecryptDataSupplier(socketChannel, sslEngine));
    }

    public void startHandshake() throws IOException {
        TLSHandshakeHelper.startHandshake(socketChannel, sslEngine);
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return dataReader.fastRead(dst);
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

    public InputStream inputStream() {
        if (in == null) {
            in = createInputStream();
        }
        return in;
    }

    private InputStream createInputStream() {
        //todo close 方法是否需要关闭 socket ?
        return new InputStream() {

            @Override
            public int read() throws IOException {
                return dataReader.inputStreamFastRead();
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return dataReader.fastRead(b, off, len);
            }

            @Override
            public long transferTo(OutputStream out) throws IOException {
                var o = new OutputStreamDataConsumer(out);
                dataReader.read(o, Integer.MAX_VALUE);
                return o.byteCount();
            }

        };
    }

}

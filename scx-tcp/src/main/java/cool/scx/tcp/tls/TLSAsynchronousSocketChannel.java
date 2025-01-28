package cool.scx.tcp.tls;

import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TLSAsynchronousSocketChannel extends AbstractAsynchronousSocketChannel {

    private final SSLEngine sslEngine;

    public TLSAsynchronousSocketChannel(AsynchronousSocketChannel socketChannel, SSLEngine sslEngine) {
        super(socketChannel);
        this.sslEngine = sslEngine;
    }

    @Override
    public <A> void read(ByteBuffer dst, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> handler) {
        
    }

    @Override
    public Future<Integer> read(ByteBuffer dst) {
        return null;
    }

    @Override
    public <A> void read(ByteBuffer[] dsts, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> handler) {

    }

    @Override
    public <A> void write(ByteBuffer src, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> handler) {

    }

    @Override
    public Future<Integer> write(ByteBuffer src) {
        return null;
    }

    @Override
    public <A> void write(ByteBuffer[] srcs, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> handler) {

    }

    @Override
    public void close() throws IOException {

    }

    public void startHandshake() {
        
    }

    public SSLEngine sslEngine() {
        return sslEngine;
    }
    
}

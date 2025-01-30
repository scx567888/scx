package cool.scx.tcp.tls;

import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//todo 未完成
public class TLSAsynchronousSocketChannel extends AsynchronousSocketChannel {

    public TLSAsynchronousSocketChannel(AsynchronousSocketChannel socketChannel, SSLEngine sslEngine) {
        super(socketChannel.provider());
    }

    @Override
    public AsynchronousSocketChannel bind(SocketAddress local) throws IOException {
        return null;
    }

    @Override
    public <T> AsynchronousSocketChannel setOption(SocketOption<T> name, T value) throws IOException {
        return null;
    }

    @Override
    public <T> T getOption(SocketOption<T> name) throws IOException {
        return null;
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        return Set.of();
    }

    @Override
    public AsynchronousSocketChannel shutdownInput() throws IOException {
        return null;
    }

    @Override
    public AsynchronousSocketChannel shutdownOutput() throws IOException {
        return null;
    }

    @Override
    public SocketAddress getRemoteAddress() throws IOException {
        return null;
    }

    @Override
    public <A> void connect(SocketAddress remote, A attachment, CompletionHandler<Void, ? super A> handler) {

    }

    @Override
    public Future<Void> connect(SocketAddress remote) {
        return null;
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
    public SocketAddress getLocalAddress() throws IOException {
        return null;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void close() throws IOException {

    }

    public void startHandshake() {

    }

    public SSLEngine sslEngine() {
        return null;
    }

}

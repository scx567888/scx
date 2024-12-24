package cool.scx.tcp.tls;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Set;

public abstract class AbstractSocketChannel extends SocketChannel {

    protected final SocketChannel socketChannel;

    public AbstractSocketChannel(SocketChannel socketChannel) {
        super(socketChannel.provider());
        this.socketChannel = socketChannel;
    }

    @Override
    public SocketChannel bind(SocketAddress local) throws IOException {
        socketChannel.bind(local);
        return this;
    }

    @Override
    public <T> SocketChannel setOption(SocketOption<T> name, T value) throws IOException {
        socketChannel.setOption(name, value);
        return this;
    }

    @Override
    public <T> T getOption(SocketOption<T> name) throws IOException {
        return socketChannel.getOption(name);
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        return socketChannel.supportedOptions();
    }

    @Override
    public SocketChannel shutdownInput() throws IOException {
        return socketChannel.shutdownInput();
    }

    @Override
    public SocketChannel shutdownOutput() throws IOException {
        return socketChannel.shutdownOutput();
    }

    @Override
    public Socket socket() {
        return socketChannel.socket();
    }

    @Override
    public boolean isConnected() {
        return socketChannel.isConnected();
    }

    @Override
    public boolean isConnectionPending() {
        return socketChannel.isConnectionPending();
    }

    @Override
    public boolean connect(SocketAddress remote) throws IOException {
        return socketChannel.connect(remote);
    }

    @Override
    public boolean finishConnect() throws IOException {
        return socketChannel.finishConnect();
    }

    @Override
    public SocketAddress getRemoteAddress() throws IOException {
        return socketChannel.getRemoteAddress();
    }

    @Override
    public SocketAddress getLocalAddress() throws IOException {
        return socketChannel.getLocalAddress();
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        socketChannel.close();
    }

    @Override
    protected void implConfigureBlocking(boolean block) throws IOException {
        socketChannel.configureBlocking(block);
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        var n = 0L;
        for (int i = offset; i < length; i = i + 1) {
            n += this.read(dsts[i]);
        }
        return n;
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        var n = 0L;
        for (int i = offset; i < length; i = i + 1) {
            n += this.write(srcs[i]);
        }
        return n;
    }

}

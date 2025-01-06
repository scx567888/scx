package cool.scx.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.SocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

public class NioTCPSocket implements ScxTCPSocket {

    private final SocketChannel socketChannel;
    private final OutputStream out;
    private final InputStream in;

    public NioTCPSocket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.in = Channels.newInputStream(socketChannel);
        this.out = Channels.newOutputStream(socketChannel);
    }

    @Override
    public InputStream inputStream() {
        return in;
    }

    @Override
    public OutputStream outputStream() {
        return out;
    }

    @Override
    public SocketAddress remoteAddress()  {
        try {
            return socketChannel.getRemoteAddress();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public SocketAddress localAddress() {
        try {
            return socketChannel.getLocalAddress();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean isClosed() {
        return !socketChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        socketChannel.close();
    }

}

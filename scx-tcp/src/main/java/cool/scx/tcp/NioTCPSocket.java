package cool.scx.tcp;

import cool.scx.tcp.tls.TLSSocketChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

public class NioTCPSocket implements ScxTCPSocket {

    private final SocketChannel socketChannel;
    private final OutputStream out;
    private final InputStream in;

    public NioTCPSocket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        if (socketChannel instanceof TLSSocketChannel tlsSocketChannel) {
            this.in = tlsSocketChannel.inputStream();
        } else {
            this.in = Channels.newInputStream(socketChannel);
        }
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
    public boolean isClosed() {
        return !socketChannel.isOpen();
    }

    @Override
    public SocketAddress remoteAddress() throws IOException {
        return socketChannel.getRemoteAddress();
    }

    @Override
    public void close() throws IOException {
        socketChannel.close();
    }

}

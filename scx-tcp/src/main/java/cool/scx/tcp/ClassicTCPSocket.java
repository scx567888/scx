package cool.scx.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.SocketAddress;


/**
 * 经典 TCP Socket
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ClassicTCPSocket implements ScxTCPSocket {

    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;

    public ClassicTCPSocket(Socket socket) {
        this.socket = socket;
        try {
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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
        return socket.isClosed();
    }

    @Override
    public SocketAddress remoteAddress() {
        return socket.getRemoteSocketAddress();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

}

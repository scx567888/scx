package cool.scx.net;

import cool.scx.io.IOStreamDataChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class ScxTCPSocketImpl2 extends IOStreamDataChannel implements ScxTCPSocket {

    private final Socket socket;

    public ScxTCPSocketImpl2(Socket socket) {
        super(getInputStream(socket), getOutputStream(socket));
        this.socket = socket;
    }

    private static InputStream getInputStream(Socket socket) {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static OutputStream getOutputStream(Socket socket) {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return socket.getRemoteSocketAddress();
    }

}

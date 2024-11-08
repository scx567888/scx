package cool.scx.net;

import cool.scx.io.InputSource;
import cool.scx.io.input_source.InputStreamInputSource;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.SocketAddress;

public class ClassicTCPSocket implements ScxTCPSocket {

    private final Socket socket;
    private final InputSource in;
    private final OutputStream out;

    public ClassicTCPSocket(Socket socket) {
        this.socket = socket;
        try {
            this.in = new InputStreamInputSource(socket.getInputStream());
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public InputSource inputSource() {
        return in;
    }

    @Override
    public OutputStream outputStream() {
        return out;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public SocketAddress remoteAddress() {
        return socket.getRemoteSocketAddress();
    }

}

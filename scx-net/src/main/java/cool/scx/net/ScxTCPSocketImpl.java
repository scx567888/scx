package cool.scx.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;

public class ScxTCPSocketImpl implements ScxTCPSocket {

    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public ScxTCPSocketImpl(Socket socket) {
        this.socket = socket;
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Socket socket() {
        return socket;
    }

    @Override
    public InputStream inputStream() {
        return inputStream;
    }

    @Override
    public OutputStream outputStream() {
        return outputStream;
    }

}

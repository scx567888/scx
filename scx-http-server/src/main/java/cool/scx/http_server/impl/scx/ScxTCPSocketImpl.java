package cool.scx.http_server.impl.scx;

import cool.scx.http_server.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ScxTCPSocketImpl implements ScxTCPSocket {

    public ScxTCPSocketImpl(Socket socket) {

    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}

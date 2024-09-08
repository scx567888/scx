package cool.scx.http_server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ScxTCPSocket {

    private final Socket socket;

    public ScxTCPSocket(Socket socket) {
        this.socket = socket;
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public void close() throws IOException {
         socket.close();
    }
    
}

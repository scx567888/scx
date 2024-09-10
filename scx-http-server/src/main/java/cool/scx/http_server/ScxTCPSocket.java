package cool.scx.http_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ScxTCPSocket {

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    void close() throws IOException;

}

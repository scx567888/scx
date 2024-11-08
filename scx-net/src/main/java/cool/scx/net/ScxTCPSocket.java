package cool.scx.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketAddress;

/**
 * ScxTCPSocket
 */
public interface ScxTCPSocket {

    InputStream inputStream();

    OutputStream outputStream();

    void close() throws IOException;

    boolean isClosed();

    SocketAddress remoteAddress() throws IOException;

}

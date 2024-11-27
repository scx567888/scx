package cool.scx.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketAddress;

/**
 * ScxTCPSocket
 */
public interface ScxTCPSocket extends Closeable {

    InputStream inputStream();

    OutputStream outputStream();

    boolean isClosed();

    SocketAddress remoteAddress() throws IOException;

}

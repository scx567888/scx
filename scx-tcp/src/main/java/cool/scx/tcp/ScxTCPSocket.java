package cool.scx.tcp;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketAddress;

/**
 * ScxTCPSocket
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxTCPSocket extends Closeable {

    InputStream inputStream();

    OutputStream outputStream();

    boolean isClosed();

    SocketAddress remoteAddress() throws IOException;

}

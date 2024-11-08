package cool.scx.net;

import cool.scx.io.InputSource;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;

/**
 * ScxTCPSocket
 */
public interface ScxTCPSocket {

    InputSource inputSource();

    OutputStream outputStream();

    void close() throws IOException;

    boolean isClosed();

    SocketAddress remoteAddress() throws IOException;

}

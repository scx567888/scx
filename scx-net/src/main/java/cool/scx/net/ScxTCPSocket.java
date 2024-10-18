package cool.scx.net;

import cool.scx.io.DataChannel;

import java.io.IOException;
import java.net.SocketAddress;

public interface ScxTCPSocket extends DataChannel {

    SocketAddress getRemoteAddress() throws IOException;

}

package cool.scx.tcp;

import java.util.function.Consumer;

/**
 * ScxTCPServer
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxTCPServer {

    ScxTCPServer onConnect(Consumer<ScxTCPSocket> connectHandler);

    void start();

    void stop();

    int port();

}

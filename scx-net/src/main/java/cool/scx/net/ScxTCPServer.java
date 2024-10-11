package cool.scx.net;

import java.util.function.Consumer;

public interface ScxTCPServer {

    ScxTCPServer onConnect(Consumer<ScxTCPSocket> connectHandler);

    void start();

    void stop();

    int port();

}

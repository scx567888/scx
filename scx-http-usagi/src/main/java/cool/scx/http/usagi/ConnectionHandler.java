package cool.scx.http.usagi;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.tcp.ScxTCPSocket;

import java.util.function.Consumer;

public interface ConnectionHandler {

    String supportedApplicationProtocol();

    void handle(ScxTCPSocket tcpSocket, UsagiHttpServerOptions options, Consumer<ScxHttpServerRequest> requestHandler);

}

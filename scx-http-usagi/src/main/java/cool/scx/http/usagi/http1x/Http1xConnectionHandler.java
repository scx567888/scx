package cool.scx.http.usagi.http1x;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.usagi.ConnectionHandler;
import cool.scx.http.usagi.UsagiHttpServerOptions;
import cool.scx.tcp.ScxTCPSocket;

import java.util.function.Consumer;

public class Http1xConnectionHandler implements ConnectionHandler {

    @Override
    public String supportedApplicationProtocol() {
        return "http/1.1";
    }

    @Override
    public void handle(ScxTCPSocket tcpSocket, UsagiHttpServerOptions options, Consumer<ScxHttpServerRequest> requestHandler) {
        new Http1xConnection(tcpSocket, options, requestHandler).start();
    }

}

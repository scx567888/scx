package cool.scx.http.usagi;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxServerWebSocket;
import cool.scx.net.ScxTCPServer;
import cool.scx.net.ScxTCPSocket;
import cool.scx.net.TCPServer;

import java.util.function.Consumer;

public class UsagiHttpServer implements ScxHttpServer {

    private final ScxTCPServer tcpServer;
    private final UsagiHttpServerOptions options;
    Consumer<ScxHttpServerRequest> requestHandler;
    private Consumer<ScxServerWebSocket> webSocketHandler;
    private Consumer<Throwable> errorHandler;

    public UsagiHttpServer(UsagiHttpServerOptions options) {
        this.options = options;
        this.tcpServer = new TCPServer(options);
        this.tcpServer.onConnect(this::handle);
    }

    public UsagiHttpServer() {
        this(new UsagiHttpServerOptions());
    }

    private void handle(ScxTCPSocket scxTCPSocket) {
        new Http1ConnectionHandler(scxTCPSocket, this).start();
    }

    @Override
    public ScxHttpServer requestHandler(Consumer<ScxHttpServerRequest> handler) {
        this.requestHandler = handler;
        return this;
    }

    @Override
    public ScxHttpServer webSocketHandler(Consumer<ScxServerWebSocket> handler) {
        this.webSocketHandler = handler;
        return this;
    }

    @Override
    public ScxHttpServer errorHandler(Consumer<Throwable> handler) {
        this.errorHandler = handler;
        return this;
    }

    @Override
    public void start() {
        tcpServer.start();
    }

    @Override
    public void stop() {
        tcpServer.stop();
    }

    @Override
    public int port() {
        return tcpServer.port();
    }

}

package cool.scx.http.usagi;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.tcp.ScxTCPServer;
import cool.scx.tcp.ScxTCPSocket;
import cool.scx.tcp.TCPServer;

import java.util.function.Consumer;

/**
 * Http 服务器
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpServer implements ScxHttpServer {

    private final ScxTCPServer tcpServer;
    private final UsagiHttpServerOptions options;
    Consumer<ScxHttpServerRequest> requestHandler;

    public UsagiHttpServer(UsagiHttpServerOptions options) {
        this.options = options;
        this.tcpServer = new TCPServer(options);
        this.tcpServer.onConnect(this::handle);
    }

    public UsagiHttpServer() {
        this(new UsagiHttpServerOptions());
    }

    private void handle(ScxTCPSocket tcpSocket) {
        new Http1xConnectionHandler(tcpSocket, this).start();
    }

    @Override
    public ScxHttpServer onRequest(Consumer<ScxHttpServerRequest> requestHandler) {
        this.requestHandler = requestHandler;
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

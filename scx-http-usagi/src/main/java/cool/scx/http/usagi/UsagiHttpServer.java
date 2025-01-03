package cool.scx.http.usagi;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.usagi.http1x.Http1xConnection;
import cool.scx.tcp.ClassicTCPServer;
import cool.scx.tcp.NioTCPServer;
import cool.scx.tcp.ScxTCPServer;
import cool.scx.tcp.ScxTCPSocket;

import java.util.function.Consumer;

/**
 * Http 服务器
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpServer implements ScxHttpServer {

    private final UsagiHttpServerOptions options;
    private final ScxTCPServer tcpServer;
    private Consumer<ScxHttpServerRequest> requestHandler;

    public UsagiHttpServer(UsagiHttpServerOptions options) {
        this.options = options;
        this.tcpServer = switch (options.tcpServerType()) {
            case CLASSIC -> new ClassicTCPServer(options);
            case NIO -> new NioTCPServer(options);
        };
        this.tcpServer.onConnect(this::handle);
    }

    public UsagiHttpServer() {
        this(new UsagiHttpServerOptions());
    }

    private void handle(ScxTCPSocket tcpSocket) {
        new Http1xConnection(tcpSocket, options, requestHandler).start();
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

package cool.scx.http.helidon;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.web_socket.ScxServerWebSocket;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.websocket.WsConfig;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

import static cool.scx.http.helidon.HelidonHelper.createHelidonWebSocketRouting;

/**
 * HelidonHttpServer
 *
 * @author scx567888
 * @version 0.0.1
 */
public class HelidonHttpServer implements ScxHttpServer {

    private final WebServer webServer;
    private final HelidonHttpServerOptions options;
    Consumer<ScxHttpServerRequest> requestHandler;
    Consumer<ScxServerWebSocket> webSocketHandler;

    public HelidonHttpServer(HelidonHttpServerOptions options) {
        this.options = options;
        var httpRouting = new HelidonHttpRouting(this);
        var webSocketRouting = createHelidonWebSocketRouting(this);
        var builder = WebServer.builder()
                .addRouting(httpRouting)
                .addRouting(webSocketRouting)
                .maxPayloadSize(options.maxPayloadSize())
                .addProtocol(
                        WsConfig.builder()
                                .maxFrameLength(options.maxWebSocketFrameLength())
                                .build()
                )
                .port(options.port());
        if (options.tls() != null) {
            builder.tls(options.tls());
        }
        this.webServer = builder.build();
        this.requestHandler = null;
        this.webSocketHandler = null;
    }

    @Override
    public HelidonHttpServer onRequest(Consumer<ScxHttpServerRequest> requestHandler) {
        this.requestHandler = requestHandler;
        return this;
    }

    @Override
    public ScxHttpServer onWebSocket(Consumer<ScxServerWebSocket> handler) {
        this.webSocketHandler = handler;
        return this;
    }

    @Override
    public void start() {
        this.webServer.start();
    }

    @Override
    public void stop() {
        this.webServer.stop();
    }

    @Override
    public InetSocketAddress localAddress() {
        return new InetSocketAddress(options.port());
    }

    public HelidonHttpServerOptions options() {
        return options;
    }

}

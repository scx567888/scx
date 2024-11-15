package cool.scx.http.helidon;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxServerWebSocket;
import io.helidon.common.tls.Tls;
import io.helidon.webserver.WebServer;

import java.util.function.Consumer;

import static cool.scx.http.helidon.HelidonHelper.createHelidonWebSocketRouting;

/**
 * HelidonHttpServer
 */
public class HelidonHttpServer implements ScxHttpServer {

    private final WebServer webServer;
    private final HelidonHttpServerOptions options;
    Consumer<ScxHttpServerRequest> requestHandler;
    Consumer<ScxServerWebSocket> webSocketHandler;
    Consumer<Throwable> errorHandler;

    public HelidonHttpServer(HelidonHttpServerOptions options) {
        this.options = options;
        var httpRouting = new HelidonHttpRouting(this);
        var webSocketRouting = createHelidonWebSocketRouting(this);
        var builder = WebServer.builder()
                .addRouting(httpRouting)
                .addRouting(webSocketRouting)
                .maxPayloadSize(options.maxPayloadSize())
                .port(options.port());
        if (options.tls() != null) {
            builder.tls((Tls) options.tls());
        }
        this.webServer = builder.build();
        this.requestHandler = null;
        this.webSocketHandler = null;
        this.errorHandler = null;
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
        this.webServer.start();
    }

    @Override
    public void stop() {
        this.webServer.stop();
    }

    @Override
    public int port() {
        return this.webServer.port();
    }

    public HelidonHttpServerOptions options() {
        return options;
    }

}

package cool.scx.http.helidon;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxServerWebSocket;
import io.helidon.webserver.WebServer;

import java.util.function.Consumer;

import static cool.scx.http.helidon.HelidonHelper.createHelidonWebSocketRouting;

public class HelidonHttpServer implements ScxHttpServer {

    private final WebServer webServer;
    Consumer<ScxHttpServerRequest> requestHandler;
    Consumer<ScxServerWebSocket> webSocketHandler;
    Consumer<Throwable> exceptionHandler;

    public HelidonHttpServer(ScxHttpServerOptions options) {
        var httpRouting = new HelidonHttpRouting(this);
        var webSocketRouting = createHelidonWebSocketRouting(this);
        this.webServer = WebServer.builder()
                .addRouting(httpRouting)
                .addRouting(webSocketRouting)
                .port(options.getPort())
                .build();
        this.requestHandler = null;
        this.webSocketHandler = null;
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
    public ScxHttpServer exceptionHandler(Consumer<Throwable> handler) {
        this.exceptionHandler = handler;
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

}

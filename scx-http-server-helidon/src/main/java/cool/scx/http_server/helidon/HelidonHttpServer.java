package cool.scx.http_server.helidon;

import cool.scx.http_server.ScxHttpRequest;
import cool.scx.http_server.ScxHttpServer;
import cool.scx.http_server.ScxHttpServerOptions;
import cool.scx.http_server.ScxWebSocket;
import io.helidon.webserver.WebServer;

import java.util.function.Consumer;

public class HelidonHttpServer implements ScxHttpServer {

    private final WebServer webServer;
    private final HelidonHttpRouting httpRouting;
    Consumer<ScxHttpRequest> requestHandler;
    Consumer<ScxWebSocket> webSocketHandler;
    Consumer<Throwable> exceptionHandler;

    public HelidonHttpServer(ScxHttpServerOptions options) {
        this.httpRouting = new HelidonHttpRouting(this);
        this.webServer = WebServer.builder()
                .routing(this.httpRouting)
                .port(options.getPort())
                .build();
        this.requestHandler = null;
    }

    @Override
    public ScxHttpServer requestHandler(Consumer<ScxHttpRequest> handler) {
        this.requestHandler = handler;
        return this;
    }

    @Override
    public ScxHttpServer webSocketHandler(Consumer<ScxWebSocket> handler) {
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

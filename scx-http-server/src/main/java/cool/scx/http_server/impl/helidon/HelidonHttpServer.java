package cool.scx.http_server.impl.helidon;

import cool.scx.http_server.ScxHttpRequest;
import cool.scx.http_server.ScxHttpServer;
import cool.scx.http_server.ScxWebSocket;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

import java.util.function.Consumer;

public class HelidonHttpServer implements ScxHttpServer {

    private final WebServer webServer;
    private final HelidonHttpRouting httpRouting;
    Consumer<ScxHttpRequest> requestHandler;

    public HelidonHttpServer() {
        this.httpRouting = new HelidonHttpRouting(this);
        this.webServer = WebServer.builder().routing(this.httpRouting).build();
        this.requestHandler = null;
    }

    @Override
    public ScxHttpServer requestHandler(Consumer<ScxHttpRequest> handler) {
        this.requestHandler = handler;
        return this;
    }

    @Override
    public ScxHttpServer webSocketHandler(Consumer<ScxWebSocket> handler) {
        return null;
    }

    @Override
    public ScxHttpServer exceptionHandler(Consumer<Throwable> handler) {
        return null;
    }

    @Override
    public void start() {
        webServer.start();
    }

    @Override
    public void stop() {
        webServer.stop();
    }

}

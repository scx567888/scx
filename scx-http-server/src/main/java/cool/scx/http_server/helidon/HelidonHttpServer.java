package cool.scx.http_server.helidon;

import cool.scx.http_server.ScxHttpRequest;
import cool.scx.http_server.ScxHttpServer;
import cool.scx.http_server.ScxHttpServerOptions;
import cool.scx.http_server.ScxWebSocket;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebServerConfig;

import java.util.function.Consumer;

public class HelidonHttpServer implements ScxHttpServer {

    private final WebServer webServer;

    public HelidonHttpServer(ScxHttpServerOptions options) {
        this.webServer=WebServer.create(WebServerConfig.builder().buildPrototype());
    }

    @Override
    public void requestHandler(Consumer<ScxHttpRequest> handler) {
        
    }

    @Override
    public void webSocketHandler(Consumer<ScxWebSocket> handler) {

    }

    @Override
    public void exceptionHandler(Consumer<Throwable> handler) {

    }

    @Override
    public void start() {
        this.webServer.start();
    }

    @Override
    public void stop() {

    }
}

package cool.scx.http_server.vertx;

import cool.scx.http_server.ScxHttpRequest;
import cool.scx.http_server.ScxHttpServer;
import cool.scx.http_server.ScxHttpServerOptions;
import cool.scx.http_server.ScxWebSocket;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

import java.util.function.Consumer;

import static cool.scx.http_server.vertx.VertxHelper.await;
import static cool.scx.http_server.vertx.VertxHelper.convertOptions;

public class VertxHttpServer implements ScxHttpServer {

    private final Vertx vertx;
    private final HttpServer httpServer;

    public VertxHttpServer(Vertx vertx, ScxHttpServerOptions options) {
        this.vertx = vertx;
        this.httpServer = vertx.createHttpServer(convertOptions(options));
    }

    @Override
    public void requestHandler(Consumer<ScxHttpRequest> handler) {
        this.httpServer.requestHandler(req ->  handler.accept(new VertxHttpRequest(req)));
    }

    @Override
    public void webSocketHandler(Consumer<ScxWebSocket> handler) {
        this.httpServer.webSocketHandler(ws ->  handler.accept(new VertxWebSocket(ws)));
    }

    @Override
    public void exceptionHandler(Consumer<Throwable> handler) {
        this.httpServer.exceptionHandler(e -> Thread.ofVirtual().start(() -> handler.accept(e)));
    }

    @Override
    public void start() {
        var listenFuture = this.httpServer.listen();
        await(listenFuture);
    }

    @Override
    public void stop() {
        var closeFuture=this.httpServer.close();
        await(closeFuture);
    }

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        
    }

}

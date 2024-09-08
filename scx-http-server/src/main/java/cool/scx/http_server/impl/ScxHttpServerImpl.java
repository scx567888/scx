package cool.scx.http_server.impl;

import cool.scx.http_server.*;

import java.util.function.Consumer;

public class ScxHttpServerImpl implements ScxHttpServer {

    private final ScxHttpServerOptions options;
    private final ScxTCPServer tcpServer;
    private Consumer<ScxHttpRequest> requestHandler;

    public ScxHttpServerImpl(ScxHttpServerOptions options) {
        this.options = options;
        this.tcpServer = ScxTCPServer.create(options);
        this.tcpServer.connectHandler(this::listen);
    }

    private void listen(ScxTCPSocket scxTCPSocket) {
        //读取 http 协议头
        //读取 http head
        //读取 http body
        this.requestHandler.accept(new ScxHttpRequestImpl(scxTCPSocket));
    }

    @Override
    public ScxHttpServer requestHandler(Consumer<ScxHttpRequest> handler) {
        this.requestHandler = handler;
        return this;
    }

    @Override
    public ScxHttpServer webSocketHandler(Consumer<ScxWebSocket> handler) {
        return this;
    }

    @Override
    public ScxHttpServer exceptionHandler(Consumer<Throwable> handler) {
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

}

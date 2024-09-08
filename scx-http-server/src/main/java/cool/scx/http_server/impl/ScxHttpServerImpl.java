package cool.scx.http_server.impl;

import cool.scx.http_server.*;

import java.io.IOException;
import java.util.function.Consumer;

public class ScxHttpServerImpl implements ScxHttpServer {

    private final ScxHttpServerOptions options;
    private final ScxTCPServer tcpServer;
    private Consumer<ScxHttpRequest> requestHandler;

    public ScxHttpServerImpl(ScxHttpServerOptions options) {
        this.options = options;
        this.tcpServer = ScxTCPServer.create(options);
        this.tcpServer.connectHandler(this::hhh);
    }

    private void hhh(ScxTCPSocket scxTCPSocket) {
        try {
            var inputStream = scxTCPSocket.getInputStream();
            //读取 http 协议头
            //读取 http head
            //读取 http body
            this.requestHandler.accept(new ScxHttpRequestImpl());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestHandler(Consumer<ScxHttpRequest> handler) {
        this.requestHandler = handler;
    }

    @Override
    public void webSocketHandler(Consumer<ScxWebSocket> handler) {

    }

    @Override
    public void exceptionHandler(Consumer<Throwable> handler) {

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

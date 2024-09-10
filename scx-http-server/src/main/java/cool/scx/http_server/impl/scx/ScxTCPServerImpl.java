package cool.scx.http_server.impl.scx;

import cool.scx.http_server.ScxTCPServer;
import cool.scx.http_server.ScxTCPServerOptions;
import cool.scx.http_server.ScxTCPSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.function.Consumer;

public class ScxTCPServerImpl implements ScxTCPServer {

    private final Thread serverThread;
    private final ScxTCPServerOptions options;
    private Consumer<ScxTCPSocket> connectHandler;
    private Consumer<Throwable> exceptionHandler;
    private ServerSocket serverSocket;

    public ScxTCPServerImpl(ScxTCPServerOptions options) {
        this.options = options;
        this.serverThread = Thread.ofPlatform().unstarted(this::listen);
    }

    private void listen() {
        while (true) {
            try {
                var socket = serverSocket.accept();
                this.connectHandler.accept(new ScxTCPSocketImpl(socket));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ScxTCPServerImpl connectHandler(Consumer<ScxTCPSocket> handler) {
        this.connectHandler = handler;
        return this;
    }

    @Override
    public ScxTCPServerImpl exceptionHandler(Consumer<Throwable> handler) {
        this.exceptionHandler = handler;
        return this;
    }

    @Override
    public void start() {
        try {
            this.serverSocket = new ServerSocket();
            this.serverSocket.bind(new InetSocketAddress(options.getPort()), 100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.serverThread.start();
    }

    @Override
    public void stop() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.serverThread.interrupt();
    }

}

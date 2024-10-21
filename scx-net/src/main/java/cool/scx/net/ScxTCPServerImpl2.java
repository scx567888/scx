package cool.scx.net;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.function.Consumer;

public class ScxTCPServerImpl2 implements ScxTCPServer {

    private final ScxTCPServerOptions options;
    private final Thread serverThread;
    private Consumer<ScxTCPSocket> connectHandler;
    private ServerSocket serverSocket;
    private boolean running;

    public ScxTCPServerImpl2() {
        this(new ScxTCPServerOptions());
    }

    public ScxTCPServerImpl2(ScxTCPServerOptions options) {
        this.options = options;
        this.serverThread = Thread.ofPlatform().unstarted(this::listen);
    }

    @Override
    public ScxTCPServer onConnect(Consumer<ScxTCPSocket> connectHandler) {
        this.connectHandler = connectHandler;
        return this;
    }

    @Override
    public void start() {
        if (running) {
            throw new IllegalStateException("Server is already running");
        }

        try {
            var tls = options.tls();
            if (tls != null && tls.enabled()) {
                this.serverSocket = tls.createServerSocket();
            } else {
                this.serverSocket = new ServerSocket();
            }
            this.serverSocket.bind(new InetSocketAddress(options.port()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        running = true;

        serverThread.start();
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }

        running = false;

        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        serverThread.interrupt();
    }

    @Override
    public int port() {
        return serverSocket.getLocalPort();
    }

    private void listen() {
        while (running) {
            try {
                var socket = this.serverSocket.accept();
                Thread.ofVirtual().start(() -> {
                    try {
                        //尝试握手
                        if (socket instanceof SSLSocket sslSocket) {
                            sslSocket.startHandshake();
                        }
                        //调用处理器
                        var tcpSocket = new ScxTCPSocketImpl2(socket);
                        connectHandler.accept(tcpSocket);
                    } catch (Exception e) {
                        //暂时忽略
                    }
                });
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

}

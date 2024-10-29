package cool.scx.net;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class TCPServer implements ScxTCPServer {

    private final ScxTCPServerOptions options;
    private final Thread serverThread;
    private Consumer<ScxTCPSocket> connectHandler;
    private ServerSocket serverSocket;
    private boolean running;

    public TCPServer() {
        this(new ScxTCPServerOptions());
    }

    public TCPServer(ScxTCPServerOptions options) {
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
                Thread.ofVirtual().start(() -> handle(socket));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private void handle(Socket socket) {
        if (socket instanceof SSLSocket sslSocket) {
            try {
                sslSocket.startHandshake(); //主动调用握手 防止等到调用用户处理程序时才发现 ssl 错误
            } catch (Exception e) {
                try {
                    socket.close(); //SSL 握手失败 !!! 尝试关闭连接
                } catch (IOException _) {
                    //我们直接忽略关闭异常 !!!
                }
                return;
            }
        }

        var tcpSocket = new TCPSocket(socket);
        //调用用户处理器
        connectHandler.accept(tcpSocket);
    }

}

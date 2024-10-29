package cool.scx.net;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

import static java.lang.System.Logger.Level.ERROR;

public class TCPServer implements ScxTCPServer {

    private static final System.Logger logger = System.getLogger(TCPServer.class.getName());

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
            throw new IllegalStateException("服务器已在运行 !!!");
        }

        var tls = options.tls();

        try {
            if (tls != null && tls.enabled()) {
                this.serverSocket = tls.createServerSocket();
            } else {
                this.serverSocket = new ServerSocket();
            }
            this.serverSocket.bind(new InetSocketAddress(options.port()));
        } catch (IOException e) {
            throw new UncheckedIOException("启动失败 !!!", e);
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
            throw new UncheckedIOException("关闭失败 !!!", e);
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
        //主动调用握手 快速检测 ssl 错误 防止等到调用用户处理程序时才发现
        if (socket instanceof SSLSocket sslSocket) {
            try {
                sslSocket.startHandshake();
            } catch (Exception e) {
                logger.log(ERROR, "SSL 握手失败 : " + e.getMessage());
                try {
                    socket.close(); //SSL 握手失败 !!! 尝试关闭连接
                } catch (IOException ce) {
                    e.addSuppressed(ce);
                }
                // 我们直接忽略所有异常 !!!
                return;
            }
        }

        var tcpSocket = new TCPSocket(socket);
        //调用用户处理器
        connectHandler.accept(tcpSocket);
    }

}

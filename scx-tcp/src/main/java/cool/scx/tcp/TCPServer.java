package cool.scx.tcp;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.TRACE;


/**
 * TCPServer
 *
 * @author scx567888
 * @version 0.0.1
 */
public class TCPServer implements ScxTCPServer {

    private static final Logger LOGGER = System.getLogger(TCPServer.class.getName());

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
        this.serverThread = Thread.ofPlatform().name("TCPServer-Listener").unstarted(this::listen);
    }

    @Override
    public ScxTCPServer onConnect(Consumer<ScxTCPSocket> connectHandler) {

        if (running) {
            throw new IllegalStateException("服务器启动后, 不允许设置 连接处理器 !!!");
        }
        
        this.connectHandler = connectHandler;
        return this;
    }

    @Override
    public void start() {

        if (this.connectHandler == null) {
            throw new IllegalStateException("未设置 连接处理器 !!!");
        }

        if (running) {
            throw new IllegalStateException("服务器已在运行 !!!");
        }

        var tls = options.tls();

        try {
            if (tls != null && tls.enabled()) {
                serverSocket = tls.createServerSocket();
            } else {
                serverSocket = new ServerSocket();
            }
            serverSocket.bind(new InetSocketAddress(options.port()));
        } catch (IOException e) {
            throw new UncheckedIOException("启动服务器失败 !!!", e);
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
            throw new UncheckedIOException("关闭服务器失败 !!!", e);
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
                Thread.ofVirtual().name("TCPServer-Handler-" + socket.getRemoteSocketAddress()).start(() -> handle(socket));
            } catch (IOException e) {
                LOGGER.log(ERROR, "服务器接受连接时发生错误 !!!", e);
                stop();
            }
        }
    }

    private void handle(Socket socket) {

        try {
            // 主动调用握手 快速检测 SSL 错误 防止等到调用用户处理程序时才发现
            if (socket instanceof SSLSocket sslSocket) {
                sslSocket.startHandshake();
            }
        } catch (IOException e) {
            LOGGER.log(TRACE, "处理 TLS 握手 时发生错误 !!!", e);
            tryCloseSocket(socket);
            return;
        }

        try {
            // 调用用户处理器
            var tcpSocket = new TCPSocket(socket);
            connectHandler.accept(tcpSocket);
        } catch (Throwable e) {
            LOGGER.log(ERROR, "调用 连接处理器 时发生错误 !!!", e);
            tryCloseSocket(socket);
        }

    }

    private void tryCloseSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException ex) {
            LOGGER.log(TRACE, "关闭 Socket 发生错误 !!!", ex);
        }
    }

}

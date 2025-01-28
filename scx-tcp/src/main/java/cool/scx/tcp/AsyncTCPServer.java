package cool.scx.tcp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.TRACE;

/**
 * 异步 I/O TCP 服务器
 *
 * @author scx567888
 * @version 0.0.1
 */
public class AsyncTCPServer implements ScxTCPServer {

    private static final Logger LOGGER = System.getLogger(AsyncTCPServer.class.getName());

    private final ScxTCPServerOptions options;
    private final Thread serverThread;
    private Consumer<ScxTCPSocket> connectHandler;
    private AsynchronousServerSocketChannel serverSocket;
    private boolean running;

    public AsyncTCPServer() {
        this(new ScxTCPServerOptions());
    }

    public AsyncTCPServer(ScxTCPServerOptions options) {
        this.options = options;
        this.serverThread = Thread.ofPlatform().name("AsyncTCPServer-Listener").unstarted(this::listen);
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

        try {
            this.serverSocket = AsynchronousServerSocketChannel.open();
            this.serverSocket.bind(options.localAddress(), options.backlog());
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
    public InetSocketAddress localAddress() {
        try {
            return (InetSocketAddress) serverSocket.getLocalAddress();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void listen() {
        while (running) {
            try {
                var socket = this.serverSocket.accept().get();
                Thread.ofVirtual().name("AsyncTCPServer-Handler-" + socket.getRemoteAddress()).start(() -> handle(socket));
            } catch (IOException | InterruptedException | ExecutionException e) {
                LOGGER.log(ERROR, "服务器接受连接时发生错误 !!!", e);
                stop();
            }
        }
    }

    private void handle(AsynchronousSocketChannel socket) {

        var tcpSocket = new AsyncTCPSocket(socket);

        if (options.autoUpgradeToTLS()) {
            try {
                tcpSocket.upgradeToTLS(options.tls());
            } catch (IOException e) {
                LOGGER.log(TRACE, "升级到 TLS 时发生错误 !!!", e);
                tryCloseSocket(tcpSocket);
                return;
            }
        }

        if (tcpSocket.tlsManager() != null) {
            tcpSocket.tlsManager().setUseClientMode(false);
        }

        if (options.autoHandshake()) {
            try {
                tcpSocket.startHandshake();
            } catch (IOException e) {
                LOGGER.log(TRACE, "处理 TLS 握手 时发生错误 !!!", e);
                tryCloseSocket(tcpSocket);
                return;
            }
        }

        if (connectHandler == null) {
            LOGGER.log(ERROR, "未设置 连接处理器, 关闭连接 !!!");
            tryCloseSocket(tcpSocket);
            return;
        }

        try {
            // 调用用户处理器
            connectHandler.accept(tcpSocket);
        } catch (Throwable e) {
            LOGGER.log(ERROR, "调用 连接处理器 时发生错误 !!!", e);
            tryCloseSocket(tcpSocket);
        }

    }

    private void tryCloseSocket(ScxTCPSocket tcpSocket) {
        try {
            tcpSocket.close();
        } catch (IOException ex) {
            LOGGER.log(TRACE, "关闭 Socket 时发生错误 !!!", ex);
        }
    }

}

package cool.scx.tcp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.function.Consumer;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.TRACE;

/// TCP 服务器
///
/// @author scx567888
/// @version 0.0.1
public class TCPServer implements ScxTCPServer {

    private static final Logger LOGGER = System.getLogger(TCPServer.class.getName());

    private final TCPServerOptions options;
    private final Thread serverThread;
    private Consumer<ScxTCPSocket> connectHandler;
    private ServerSocket serverSocket;
    private volatile boolean running;

    public TCPServer() {
        this(new TCPServerOptions());
    }

    public TCPServer(TCPServerOptions options) {
        this.options = options;
        this.serverThread = Thread.ofPlatform().name("TCPServer-Listener").unstarted(this::listen);
        this.running = false;
    }

    @Override
    public ScxTCPServer onConnect(Consumer<ScxTCPSocket> connectHandler) {
        this.connectHandler = connectHandler;
        return this;
    }

    @Override
    public void start(SocketAddress localAddress) {
        if (running) {
            throw new IllegalStateException("服务器已在运行 !!!");
        }

        if (connectHandler == null) {
            throw new IllegalStateException("未设置 连接处理器 !!!");
        }

        try {
            this.serverSocket = new ServerSocket();
            this.serverSocket.bind(localAddress, options.backlog());
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

        try {
            serverThread.join();
        } catch (InterruptedException _) {
            // 这里理论上永远都不会发生
        }

    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) serverSocket.getLocalSocketAddress();
    }

    public TCPServerOptions options() {
        return options;
    }

    private void listen() {
        while (running) {
            try {
                var socket = this.serverSocket.accept();
                Thread.ofVirtual().name("TCPServer-Handler-" + socket.getRemoteSocketAddress()).start(() -> handle(socket));
            } catch (IOException e) {
                //第一种情况 服务器主动关闭
                if (!running) {
                    break;
                }
                //第二种情况 accept 出现异常 
                running = false;
                LOGGER.log(ERROR, "服务器 接受连接 时发生错误 !!!", e);
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    LOGGER.log(TRACE, "关闭 serverSocket 时发生错误 !!!", ex);
                }
                break;
            }
        }
    }

    private void handle(Socket socket) {

        var tcpSocket = new TCPSocket(socket);

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

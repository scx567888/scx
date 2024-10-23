package cool.scx.net;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.function.Consumer;

public class TCPServer implements ScxTCPServer {

    private final ScxTCPServerOptions options;
    private final Thread serverThread;
    private Consumer<ScxTCPSocket> connectHandler;
    private ServerSocketChannel serverSocketChannel;
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
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.bind(new InetSocketAddress(options.port()));
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
            serverSocketChannel.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        serverThread.interrupt();
    }

    @Override
    public int port() {
        try {
            //理论上都是 InetSocketAddress 类型
            var localAddress = (InetSocketAddress) serverSocketChannel.getLocalAddress();
            return localAddress.getPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void listen() {
        while (running) {
            try {
                var socketChannel = this.serverSocketChannel.accept();
                Thread.ofVirtual().start(() -> {
                    try {
                        var tls = options.tls();

                        if (tls != null && tls.enabled()) {
                            //创建 sslEngine
                            var sslEngine = tls.sslContext().createSSLEngine();
                            sslEngine.setUseClientMode(false);
                            //创建 TLSScxTCPSocketImpl 并执行握手
                            var tcpSocket = new TLSTCPSocket(socketChannel, sslEngine);
                            try {
                                //握手失败 目前直接 关闭远程连接
                                tcpSocket.startHandshake();
                                connectHandler.accept(tcpSocket);
                            } catch (Exception e) {
                                socketChannel.close();
                                e.printStackTrace();
                            }
                        } else {
                            var tcpSocket = new PlainTCPSocket(socketChannel);
                            //调用处理器
                            connectHandler.accept(tcpSocket);
                        }
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

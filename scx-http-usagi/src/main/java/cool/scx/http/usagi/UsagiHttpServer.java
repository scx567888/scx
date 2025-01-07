package cool.scx.http.usagi;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.tcp.ClassicTCPServer;
import cool.scx.tcp.NioTCPServer;
import cool.scx.tcp.ScxTCPServer;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.util.function.Consumer;

/**
 * Http 服务器
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpServer implements ScxHttpServer {

    private final UsagiHttpServerOptions options;
    private final ScxTCPServer tcpServer;
    private final ConnectionHandlerSelector connectionHandlerSelector;
    private Consumer<ScxHttpServerRequest> requestHandler;

    public UsagiHttpServer(UsagiHttpServerOptions options) {
        this.options = options;
        this.tcpServer = switch (options.tcpServerType()) {
            case CLASSIC -> new ClassicTCPServer(options);
            case NIO -> new NioTCPServer(options);
        };
        this.tcpServer.onConnect(this::handle);
        this.connectionHandlerSelector = new ConnectionHandlerSelector();
    }

    public UsagiHttpServer() {
        this(new UsagiHttpServerOptions());
    }

    private void handle(ScxTCPSocket tcpSocket) {
        //默认连接处理器
        var connectionHandler = connectionHandlerSelector.defaultConnectionHandler();
         
        if (tcpSocket.isTLS()) {
            //配置应用协议协商选择器
            tcpSocket.tlsConfig().setHandshakeApplicationProtocolSelector((tlsConfig, list) -> {
                for (var s : list) {
                    if (connectionHandlerSelector.containsApplicationProtocol(s)) {
                        return s;
                    }
                }
                return null;
            });
            // 开始握手
            try {
                tcpSocket.startHandshake();
            } catch (IOException e) {
                try {
                    tcpSocket.close();
                } catch (IOException ex) {
                    e.addSuppressed(ex);
                }
                throw new UncheckedIOException("TLS 握手失败 !!!!", e);
            }
            var applicationProtocol = tcpSocket.tlsConfig().getApplicationProtocol();
            connectionHandler = connectionHandlerSelector.find(applicationProtocol);
        }
        //开始连接
        connectionHandler.handle(tcpSocket, options, requestHandler);
    }

    @Override
    public ScxHttpServer onRequest(Consumer<ScxHttpServerRequest> requestHandler) {
        this.requestHandler = requestHandler;
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

    @Override
    public InetSocketAddress localAddress() {
        return tcpServer.localAddress();
    }

}

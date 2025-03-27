package cool.scx.http.x;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.x.http1.Http1ServerConnection;
import cool.scx.http.x.http2.Http2ServerConnection;
import cool.scx.tcp.ClassicTCPServer;
import cool.scx.tcp.NioTCPServer;
import cool.scx.tcp.ScxTCPServer;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.lang.System.Logger;
import java.net.InetSocketAddress;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/// Http 服务器
///
/// @author scx567888
/// @version 0.0.1
public class XHttpServer implements ScxHttpServer {

    private static final Logger LOGGER = System.getLogger(XHttpServer.class.getName());

    private final XHttpServerOptions options;
    private final ScxTCPServer tcpServer;
    private Consumer<ScxHttpServerRequest> requestHandler;
    private BiConsumer<Throwable, ScxHttpServerRequest> errorHandler;

    public XHttpServer(XHttpServerOptions options) {
        this.options = options;
        this.tcpServer = switch (options.tcpServerType()) {
            case CLASSIC -> new ClassicTCPServer(options.tcpServerOptions());
            case NIO -> new NioTCPServer(options.tcpServerOptions());
        };
        this.tcpServer.onConnect(this::handle);
    }

    public XHttpServer() {
        this(new XHttpServerOptions());
    }

    private void handle(ScxTCPSocket tcpSocket) {
        //是否使用 http2
        var useHttp2 = false;

        if (tcpSocket.isTLS()) {
            // 配置应用协议协商选择器
            tcpSocket.tlsManager().setHandshakeApplicationProtocolSelector((_, protocols) -> options.enableHttp2() && protocols.contains("h2") ? "h2" : protocols.contains("http/1.1") ? "http/1.1" : null);
            // 开始握手
            try {
                tcpSocket.startHandshake();
            } catch (IOException e) {
                try {
                    tcpSocket.close();
                } catch (IOException ex) {
                    e.addSuppressed(ex);
                }
                LOGGER.log(Logger.Level.TRACE, "处理 TLS 握手 时发生错误 !!!", e);
                return;
            }
            var applicationProtocol = tcpSocket.tlsManager().getApplicationProtocol();
            useHttp2 = "h2".equals(applicationProtocol);
        }

        if (useHttp2) {
            new Http2ServerConnection(tcpSocket, options, requestHandler, errorHandler).start();
        } else {
            //此处的Http1 特指 HTTP/1.1
            new Http1ServerConnection(tcpSocket, options, requestHandler, errorHandler).start();
        }
    }

    @Override
    public ScxHttpServer onRequest(Consumer<ScxHttpServerRequest> requestHandler) {
        this.requestHandler = requestHandler;
        return this;
    }

    @Override
    public ScxHttpServer onError(BiConsumer<Throwable, ScxHttpServerRequest> errorHandler) {
        this.errorHandler = errorHandler;
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

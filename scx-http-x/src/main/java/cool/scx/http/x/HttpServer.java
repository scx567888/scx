package cool.scx.http.x;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.error_handler.ScxHttpServerErrorHandler;
import cool.scx.http.x.http1.Http1ServerConnection;
import cool.scx.http.x.http2.Http2ServerConnection;
import cool.scx.tcp.ScxTCPServer;
import cool.scx.tcp.ScxTCPSocket;
import cool.scx.tcp.TCPServer;

import java.io.IOException;
import java.lang.System.Logger;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Consumer;

/// Http 服务器
///
/// @author scx567888
/// @version 0.0.1
public class HttpServer implements ScxHttpServer {

    private static final Logger LOGGER = System.getLogger(HttpServer.class.getName());

    private final HttpServerOptions options;
    private final ScxTCPServer tcpServer;
    private Consumer<ScxHttpServerRequest> requestHandler;
    private ScxHttpServerErrorHandler errorHandler;

    public HttpServer(HttpServerOptions options) {
        this.options = options;
        this.tcpServer = new TCPServer(options.tcpServerOptions());
        this.tcpServer.onConnect(this::handle);
    }

    public HttpServer() {
        this(new HttpServerOptions());
    }

    private static void tryCloseSocket(ScxTCPSocket tcpSocket, Exception e) {
        try {
            tcpSocket.close();
        } catch (IOException ex) {
            e.addSuppressed(ex);
        }
        LOGGER.log(Logger.Level.TRACE, "处理 TLS 握手 时发生错误 !!!", e);
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
                tryCloseSocket(tcpSocket, e);
                return;
            }
            var applicationProtocol = tcpSocket.tlsManager().getApplicationProtocol();
            useHttp2 = "h2".equals(applicationProtocol);
        }

        if (useHttp2) {
            new Http2ServerConnection(tcpSocket, options, requestHandler, errorHandler).start();
        } else {
            //此处的 Http1 特指 HTTP/1.1
            new Http1ServerConnection(tcpSocket, options, requestHandler, errorHandler).start();
        }
    }

    @Override
    public ScxHttpServer onRequest(Consumer<ScxHttpServerRequest> requestHandler) {
        this.requestHandler = requestHandler;
        return this;
    }

    @Override
    public ScxHttpServer onError(ScxHttpServerErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public void start(SocketAddress localAddress) {
        tcpServer.start(localAddress);
    }

    @Override
    public void stop() {
        tcpServer.stop();
    }

    @Override
    public InetSocketAddress localAddress() {
        return tcpServer.localAddress();
    }

    public HttpServerOptions options() {
        return options;
    }

}

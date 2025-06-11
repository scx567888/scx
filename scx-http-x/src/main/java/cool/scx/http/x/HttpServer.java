package cool.scx.http.x;

import cool.scx.functional.ScxConsumer;
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

import static cool.scx.http.version.HttpVersion.HTTP_1_1;
import static cool.scx.http.version.HttpVersion.HTTP_2;
import static java.lang.System.Logger.Level.TRACE;

/// Http 服务器
///
/// @author scx567888
/// @version 0.0.1
public class HttpServer implements ScxHttpServer {

    private static final Logger LOGGER = System.getLogger(HttpServer.class.getName());

    private final HttpServerOptions options;
    private final ScxTCPServer tcpServer;
    private ScxConsumer<ScxHttpServerRequest, ?> requestHandler;
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
    }

    private void handle(ScxTCPSocket tcpSocket) {
        //配置 tls
        if (options.tls() != null) {
            try {
                tcpSocket.upgradeToTLS(options.tls());
            } catch (IOException e) {
                tryCloseSocket(tcpSocket, e);
                LOGGER.log(TRACE, "升级到 TLS 时发生错误 !!!", e);
                return;
            }
            tcpSocket.tlsManager().setUseClientMode(false);
            tcpSocket.tlsManager().setHandshakeApplicationProtocolSelector((_, protocols) -> options.enableHttp2() && protocols.contains(HTTP_2.alpnValue()) ? HTTP_2.alpnValue() : protocols.contains(HTTP_1_1.alpnValue()) ? HTTP_1_1.alpnValue() : null);
            // 开始握手
            try {
                tcpSocket.startHandshake();
            } catch (IOException e) {
                tryCloseSocket(tcpSocket, e);
                LOGGER.log(TRACE, "处理 TLS 握手 时发生错误 !!!", e);
                return;
            }
        }

        var useHttp2 = false;

        if (tcpSocket.isTLS()) {
            var applicationProtocol = tcpSocket.tlsManager().getApplicationProtocol();
            useHttp2 = HTTP_2.alpnValue().equals(applicationProtocol);
        }

        if (useHttp2) {
            new Http2ServerConnection(tcpSocket, options, requestHandler, errorHandler).start();
        } else {
            //此处的 Http1 特指 HTTP/1.1
            new Http1ServerConnection(tcpSocket, options, requestHandler, errorHandler).start();
        }
    }

    @Override
    public ScxHttpServer onRequest(ScxConsumer<ScxHttpServerRequest, ?> requestHandler) {
        this.requestHandler = requestHandler;
        return this;
    }

    @Override
    public ScxHttpServer onError(ScxHttpServerErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public void start(SocketAddress localAddress) throws IOException {
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

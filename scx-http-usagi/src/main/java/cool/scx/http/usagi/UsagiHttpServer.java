package cool.scx.http.usagi;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.usagi.http1x.Http1xServerConnection;
import cool.scx.http.usagi.http2.Http2ServerConnection;
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
    private Consumer<ScxHttpServerRequest> requestHandler;

    public UsagiHttpServer(UsagiHttpServerOptions options) {
        this.options = options;
        this.tcpServer = switch (options.tcpServerType()) {
            case CLASSIC -> new ClassicTCPServer(options.tcpServerOptions());
            case NIO -> new NioTCPServer(options.tcpServerOptions());
        };
        this.tcpServer.onConnect(this::handle);
    }

    public UsagiHttpServer() {
        this(new UsagiHttpServerOptions());
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
                throw new UncheckedIOException("TLS 握手失败 !!!!", e);
            }
            var applicationProtocol = tcpSocket.tlsManager().getApplicationProtocol();
            useHttp2 = "h2".equals(applicationProtocol);
        }

        if (useHttp2) {
            new Http2ServerConnection(tcpSocket, options, requestHandler).start();
        } else {
            new Http1xServerConnection(tcpSocket, options, requestHandler).start();
        }
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

package cool.scx.http.peach;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import cool.scx.io.InputStreamDataSupplier;
import cool.scx.io.LinkedDataReader;
import cool.scx.net.ScxTCPServer;
import cool.scx.net.ScxTCPServerOptions;
import cool.scx.net.ScxTCPSocket;
import cool.scx.net.ClassicTCPServer;
import cool.scx.net.tls.TLS;

import java.net.URLDecoder;
import java.util.function.Consumer;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpMethod.GET;
import static java.nio.charset.StandardCharsets.UTF_8;

public class PeachHttpServer implements ScxHttpServer {

    private final ScxTCPServer tcpServer;
    private final ScxHttpServerOptions options;
    private Consumer<ScxHttpServerRequest> requestHandler;
    private Consumer<ScxServerWebSocket> webSocketHandler;
    private Consumer<Throwable> errorHandler;

    public PeachHttpServer(ScxHttpServerOptions options) {
        this.options = options;
        this.tcpServer = new ClassicTCPServer(new ScxTCPServerOptions().port(options.port()).tls((TLS) options.tls()));
        this.tcpServer.onConnect(this::handle);
    }

    public PeachHttpServer() {
        this(new ScxHttpServerOptions());
    }

    private void handle(ScxTCPSocket scxTCPSocket) {
        //先假定 这是一个 http 1.1 连接
        var dataReader = new LinkedDataReader(new InputStreamDataSupplier(scxTCPSocket.inputStream()));
        while (true) {
            //读取 请求行
            var requestLineBytes = dataReader.readUntil("\r\n".getBytes());
            String requestLine = new String(requestLineBytes);
            String[] split = requestLine.split(" ");
            if (split.length != 3) {
                throw new RuntimeException("Invalid request line: " + requestLine);
            }
            var method0 = split[0];
            var path0 = split[1];
            var version0 = split[2];

            var method = ScxHttpMethod.of(method0);
            var path = URLDecoder.decode(path0, UTF_8);
            var version = HttpVersion.of(version0);

            var headerBytes = dataReader.readUntil("\r\n\r\n".getBytes());

            var headerStr = new String(headerBytes);

            var headers = ScxHttpHeaders.of(headerStr);

            var connection = headers.get(CONNECTION);
            var upgrade = headers.get(UPGRADE);

            var isWebSocketHandshake = method == GET && "Upgrade".equals(connection) && "websocket".equals(upgrade);

            PeachHttpServerRequest request;

            if (isWebSocketHandshake) {
                request = new PeachServerWebSocketHandshakeRequest(dataReader, scxTCPSocket.outputStream());
            } else {
                request = new PeachHttpServerRequest();
            }

            request.method = method;
            request.uri = ScxURI.of(path);
            request.version = version;
            request.headers = headers;

            ScxHttpBody body = null;

            Long contentLength = headers.contentLength();

            if (contentLength != null) {
                body = new PeachScxHttpBody(dataReader, headers, contentLength);
            } else {
                body = new PeachScxHttpBody(dataReader, headers, 0L);
            }

            request.body = body;

            var response = new PeachHttpServerResponse(request, scxTCPSocket);

            response.headers().set(CONNECTION, "keep-alive");

            response.headers().set(SERVER, "Scx Peach");

            request.response = response;

            if (requestHandler != null) {
                requestHandler.accept(request);
            }

            //尝试启动 websocket 监听 todo 这里应该重新设计
            if (request instanceof PeachServerWebSocketHandshakeRequest w) {
                var ws = w.webSocket;
                if (ws != null) {
                    ws.start();
                }
                // websocket 独占整个连接 退出循环
                break;
            }

        }
    }

    @Override
    public ScxHttpServer requestHandler(Consumer<ScxHttpServerRequest> handler) {
        this.requestHandler = handler;
        return this;
    }

    @Override
    public ScxHttpServer webSocketHandler(Consumer<ScxServerWebSocket> handler) {
        this.webSocketHandler = handler;
        return this;
    }

    @Override
    public ScxHttpServer errorHandler(Consumer<Throwable> handler) {
        this.errorHandler = handler;
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
    public int port() {
        return tcpServer.port();
    }

}

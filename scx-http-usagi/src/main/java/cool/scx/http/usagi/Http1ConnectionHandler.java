package cool.scx.http.usagi;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import cool.scx.io.InputStreamDataSupplier;
import cool.scx.io.LinkedDataReader;
import cool.scx.io.NoMoreDataException;
import cool.scx.tcp.ScxTCPSocket;

import java.io.InputStream;
import java.net.URLDecoder;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpMethod.GET;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Http1ConnectionHandler {

    private static final byte[] CRLF_BYTES = "\r\n".getBytes();
    private static final byte[] CRLF_CRLF_BYTES = "\r\n\r\n".getBytes();

    private final ScxTCPSocket scxTCPSocket;
    private final UsagiHttpServer httpServer;
    private final LinkedDataReader dataReader;

    public Http1ConnectionHandler(ScxTCPSocket scxTCPSocket, UsagiHttpServer httpServer) {
        this.scxTCPSocket = scxTCPSocket;
        this.httpServer = httpServer;
        //先假定 这是一个 http 1.1 连接
        this.dataReader = new LinkedDataReader(new InputStreamDataSupplier(scxTCPSocket.inputStream()));
    }

    public void start() {
        while (true) {
            //读取 请求行 和请求头
            var requestLine = readRequestLine();
            var headers = readHeaders();

            //判断是否为 websocket 连接
            var isWebSocketHandshake = checkIsWebSocketHandshake(requestLine, headers);

            UsagiHttpServerRequest request;

            if (isWebSocketHandshake) {
                request = new UsagiServerWebSocketHandshakeRequest(scxTCPSocket, dataReader, scxTCPSocket.outputStream());
            } else {
                request = new UsagiHttpServerRequest(scxTCPSocket);
            }

            request.method = requestLine.method();
            request.uri = ScxURI.of(requestLine.path());
            request.version = requestLine.version();
            request.headers = headers;

            //此处判断请求体是不是分块传输
            var transferEncoding = headers.get(TRANSFER_ENCODING);

            ScxHttpBody body;

            if ("chunked".equals(transferEncoding)) {
                body = new ScxHttpBodyImpl(new HttpChunkedInputStream(dataReader), headers, 65535);
            } else {
                var contentLength = headers.contentLength();
                if (contentLength != null) {
                    body = new ScxHttpBodyImpl(new FixedLengthInputStream(dataReader, contentLength), headers, 65536);
                } else {
                    body = new ScxHttpBodyImpl(InputStream.nullInputStream(), headers, 65536);
                }
            }

            request.body = body;

            var response = new UsagiHttpServerResponse(request, scxTCPSocket);

            response.headers().set(CONNECTION, "keep-alive");

            response.headers().set(SERVER, "Scx Usagi");

            request.response = response;

            //尝试启动 websocket 监听 
            // todo 这里应该重新设计 以便给用户 终止握手的可能 比如 去掉 websocketHandler 而是使用判断  ScxServerWebSocketHandshakeRequest 来处理
            if (request instanceof UsagiServerWebSocketHandshakeRequest w) {
                var usagiServerWebSocket = w.webSocket();
                _callWebSocketHandler(usagiServerWebSocket);
                // websocket 独占整个连接 退出循环
                usagiServerWebSocket.start();
                break;
            } else {
                _callRequestHandler(request);
            }
        }
    }

    private void _callRequestHandler(UsagiHttpServerRequest request) {
        if (httpServer.requestHandler != null) {
            httpServer.requestHandler.accept(request);
        }
    }

    private void _callWebSocketHandler(UsagiServerWebSocket request) {
        if (httpServer.webSocketHandler != null) {
            httpServer.webSocketHandler.accept(request);
        }
    }

    private HttpRequestLine readRequestLine() {
        byte[] requestLineBytes;
        try {
            //这里发生 NoMoreDataException 只有一种情况就是 socket 关闭了
            requestLineBytes = dataReader.readUntil(CRLF_BYTES);
        } catch (NoMoreDataException e) {
            throw new RuntimeException("socket 关闭了 !!!");
        }

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
        return new HttpRequestLine(method, path, version);
    }

    private ScxHttpHeadersWritable readHeaders() {
        var headerBytes = dataReader.readUntil(CRLF_CRLF_BYTES);
        var headerStr = new String(headerBytes);
        return ScxHttpHeaders.of(headerStr);
    }

    public boolean checkIsWebSocketHandshake(HttpRequestLine requestLine, ScxHttpHeaders headers) {
        if (requestLine.method() == GET) {
            var connection = headers.get(CONNECTION);
            if ("Upgrade".equals(connection)) {
                var upgrade = headers.get(UPGRADE);
                return "websocket".equals(upgrade);
            }
        }
        return false;
    }

    public void stop() {

    }

}

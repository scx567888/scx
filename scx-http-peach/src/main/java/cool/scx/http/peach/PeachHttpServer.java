package cool.scx.http.peach;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import cool.scx.io.LinkedDataReader;
import cool.scx.net.ScxTCPServer;
import cool.scx.net.ScxTCPServerOptions;
import cool.scx.net.ScxTCPSocket;
import cool.scx.net.TCPServer;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.function.Consumer;
import java.util.function.Function;

import static cool.scx.http.HttpFieldName.CONNECTION;
import static cool.scx.http.HttpFieldName.SERVER;
import static java.nio.charset.StandardCharsets.UTF_8;

public class PeachHttpServer implements ScxHttpServer {

    private final ScxTCPServer tcpServer;
    private final ScxHttpServerOptions options;
    private Consumer<ScxHttpServerRequest> requestHandler;
    private Consumer<ScxServerWebSocket> webSocketHandler;
    private Consumer<Throwable> errorHandler;

    public PeachHttpServer(ScxHttpServerOptions options) {
        this.options = options;
        this.tcpServer = new TCPServer(new ScxTCPServerOptions().port(options.port()));
        this.tcpServer.onConnect(this::listen);
    }

    public PeachHttpServer() {
        this(new ScxHttpServerOptions());
    }

    private void listen(ScxTCPSocket scxTCPSocket) {
        var dataReader = new LinkedDataReader(() -> {
            try {
                byte[] read = scxTCPSocket.read(8192);
                return new LinkedDataReader.Node(read);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        while (true) {
            //读取 请求行
            var requestLineBytes = dataReader.readUntil("\r\n".getBytes());
            String requestLine = new String(requestLineBytes);
            String[] split = requestLine.split(" ");
            if (split.length != 3) {
                throw new RuntimeException("Invalid request line: " + requestLine);
            }
            var method = split[0];
            var path = split[1];
            var version = split[2];

            var request = new PeachHttpServerRequest();

            request.method = ScxHttpMethod.of(method);
            request.uri = ScxURI.of(URLDecoder.decode(path, UTF_8));
            request.version = HttpVersion.of(version);

            var headerBytes = dataReader.readUntil("\r\n\r\n".getBytes());

            var headerStr = new String(headerBytes);

            var headers = ScxHttpHeaders.of(headerStr);

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

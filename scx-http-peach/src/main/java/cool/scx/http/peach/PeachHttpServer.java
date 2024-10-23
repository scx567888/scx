package cool.scx.http.peach;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import cool.scx.io.LinkedDataReader;
import cool.scx.net.ScxTCPServer;
import cool.scx.net.ScxTCPServerOptions;
import cool.scx.net.ScxTCPSocket;
import cool.scx.net.TCPServer;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class PeachHttpServer implements ScxHttpServer {

    private final Function<ScxTCPServerOptions, ScxTCPServer> tcpServerBuilder;
    private final ScxTCPServer tcpServer;
    private final ScxHttpServerOptions options;
    private Consumer<ScxHttpServerRequest> requestHandler;
    private Consumer<ScxServerWebSocket> webSocketHandler;
    private Consumer<Throwable> errorHandler;

    public PeachHttpServer(ScxHttpServerOptions options, Function<ScxTCPServerOptions, ScxTCPServer> tcpServerBuilder) {
        this.options = options;
        this.tcpServerBuilder = tcpServerBuilder;
        this.tcpServer = tcpServerBuilder.apply(new ScxTCPServerOptions().port(options.port()));
        this.tcpServer.onConnect(this::listen);
    }

    public PeachHttpServer(ScxHttpServerOptions options) {
        this(options, TCPServer::new);
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

            request.method = HttpMethod.of(method);
            request.uri = ScxURI.of(path);
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

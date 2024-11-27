package cool.scx.http.helidon;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.web_socket.ScxClientWebSocket;
import cool.scx.http.web_socket.ScxClientWebSocketBuilder;
import io.helidon.webclient.websocket.WsClient;

import java.util.function.Consumer;

class HelidonClientWebSocketBuilder implements ScxClientWebSocketBuilder {

    Consumer<ScxClientWebSocket> connectHandler;
    private ScxURIWritable uri;
    private ScxHttpHeadersWritable headers;

    public HelidonClientWebSocketBuilder(HelidonHttpClient httpClient) {
        this.headers = ScxHttpHeaders.of();
    }

    @Override
    public ScxURIWritable uri() {
        return uri;
    }

    @Override
    public ScxClientWebSocketBuilder uri(ScxURI uri) {
        this.uri = ScxURI.of(uri);
        return this;
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return headers;
    }

    @Override
    public ScxClientWebSocketBuilder headers(ScxHttpHeaders headers) {
        this.headers = ScxHttpHeaders.of(headers);
        return this;
    }

    @Override
    public ScxClientWebSocketBuilder onConnect(Consumer<ScxClientWebSocket> onConnect) {
        this.connectHandler = onConnect;
        return this;
    }

    @Override
    public void connect() {
        var wsClientBuilder = WsClient.builder();
        for (var i : headers) {
            var key = i.getKey();
            var values = i.getValue();
            for (var value : values) {
                wsClientBuilder.addHeader(key.value(), value);
            }
        }
        var wsClient = wsClientBuilder.build();

        wsClient.connect(uri.toURI(), new HelidonClientWebSocket(this));
    }

}

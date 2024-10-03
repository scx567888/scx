package cool.scx.http.helidon;

import cool.scx.http.ScxClientWebSocket;
import cool.scx.http.ScxClientWebSocketBuilder;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.uri.ScxURI;
import io.helidon.common.uri.UriEncoding;
import io.helidon.webclient.websocket.WsClient;
import io.helidon.websocket.WsSession;

import java.net.URI;
import java.util.function.Consumer;

/**
 * HelidonClientWebSocket
 */
class HelidonClientWebSocket extends HelidonWebSocket implements ScxClientWebSocket, ScxClientWebSocketBuilder {

    private Consumer<ScxClientWebSocket> connectHandler;
    private ScxURI uri;
    private ScxHttpHeadersWritable builderHeaders;

    public HelidonClientWebSocket() {
        builderHeaders = ScxHttpHeaders.of();
    }

    @Override
    public ScxURI uri() {
        return uri;
    }

    @Override
    public HelidonClientWebSocket uri(ScxURI uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return builderHeaders;
    }

    @Override
    public ScxClientWebSocketBuilder headers(ScxHttpHeadersWritable headers) {
        builderHeaders = headers;
        return this;
    }

    @Override
    public void onOpen(WsSession session) {
        super.onOpen(session);
        if (connectHandler != null) {
            connectHandler.accept(this);
        }
    }

    @Override
    public HelidonClientWebSocket onConnect(Consumer<ScxClientWebSocket> connectHandler) {
        this.connectHandler = connectHandler;
        return this;
    }

    @Override
    public void connect() {
        var wsClientBuilder = WsClient.builder();
        for (var i : builderHeaders) {
            var key = i.getKey();
            var values = i.getValue();
            for (var value : values) {
                wsClientBuilder.addHeader(key.value(), value);
            }
        }
        var wsClient = wsClientBuilder.build();
        wsClient.connect(uri.toURI(), this);
    }

}

package cool.scx.http;

import cool.scx.http.uri.ScxURI;

import java.util.function.Consumer;

/**
 * ScxClientWebSocketBuilder
 */
public interface ScxClientWebSocketBuilder {

    ScxURI uri();

    ScxClientWebSocketBuilder uri(ScxURI uri);

    ScxClientWebSocketBuilder onConnect(Consumer<ScxClientWebSocket> onOpen);

    void connect();

    default ScxClientWebSocketBuilder uri(String uri) {
        return uri(ScxURI.of(uri));
    }

}

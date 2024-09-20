package cool.scx.http;

import cool.scx.http.uri.ScxURI;

import java.util.function.Consumer;

/**
 * ScxClientWebSocket
 */
public interface ScxClientWebSocket extends ScxWebSocket {

    ScxURI uri();

    ScxClientWebSocket uri(ScxURI uri);

    ScxClientWebSocket onOpen(Consumer<ScxClientWebSocket> onOpen);

    ScxClientWebSocket content();

    default ScxClientWebSocket uri(String uri) {
        return uri(ScxURI.of(uri));
    }

}

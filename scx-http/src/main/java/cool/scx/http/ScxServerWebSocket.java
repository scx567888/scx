package cool.scx.http;

import cool.scx.http.uri.ScxURI;

/**
 * ScxServerWebSocket
 */
public interface ScxServerWebSocket extends ScxWebSocket {

    ScxURI uri();

    ScxHttpHeaders headers();

    default String path() {
        return uri().path();
    }

    default Parameters<String, String> query() {
        return uri().query();
    }

}

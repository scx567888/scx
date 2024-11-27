package cool.scx.http.web_socket;

import cool.scx.http.Parameters;
import cool.scx.http.ScxHttpHeaders;
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
package cool.scx.http;

import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.URIPath;

/**
 * ScxServerWebSocket
 */
public interface ScxServerWebSocket extends ScxWebSocket {

    ScxURI uri();

    ScxHttpHeaders headers();

    default URIPath path() {
        return uri().path();
    }

    default Parameters<String, String> query() {
        return uri().query();
    }

}

package cool.scx.http;

import cool.scx.http.uri.URI;
import cool.scx.http.uri.URIPath;
import cool.scx.http.uri.URIQuery;

/**
 * ScxServerWebSocket
 */
public interface ScxServerWebSocket extends ScxWebSocket {

    URI uri();

    ScxHttpHeaders headers();

    default URIPath path() {
        return uri().path();
    }

    default URIQuery query() {
        return uri().query();
    }

}

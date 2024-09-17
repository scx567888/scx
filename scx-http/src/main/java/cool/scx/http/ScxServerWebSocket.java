package cool.scx.http;

import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.URIPath;
import cool.scx.http.uri.URIQuery;

/**
 * ScxServerWebSocket
 */
public interface ScxServerWebSocket extends ScxWebSocket {

    ScxURI uri();

    ScxHttpHeaders headers();

    default URIPath path() {
        return uri().path();
    }

    default URIQuery query() {
        return uri().query();
    }

}

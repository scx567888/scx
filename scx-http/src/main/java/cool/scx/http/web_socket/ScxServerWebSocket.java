package cool.scx.http.web_socket;

import cool.scx.http.Parameters;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.uri.ScxURI;

/// ScxServerWebSocket
///
/// @author scx567888
/// @version 0.0.1
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

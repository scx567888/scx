package cool.scx.http;

import cool.scx.http.headers.ScxHttpHeadersReadHelper;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.parameters.Parameters;
import cool.scx.http.peer_info.PeerInfo;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.version.HttpVersion;

/// ScxHttpServerRequest
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpServerRequest extends ScxHttpHeadersReadHelper {

    ScxHttpServerResponse response();

    ScxHttpMethod method();

    ScxURI uri();

    HttpVersion version();

    @Override
    ScxHttpHeaders headers();

    ScxHttpBody body();

    PeerInfo remotePeer();

    PeerInfo localPeer();

    //******************** 简化 URI 操作 *******************

    default String path() {
        return uri().path();
    }

    default Parameters<String, String> query() {
        return uri().query();
    }

    default String getQuery(String name) {
        return uri().getQuery(name);
    }

}

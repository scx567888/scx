package cool.scx.http;

import cool.scx.http.uri.URIPath;
import cool.scx.http.uri.URIQuery;

public interface ScxHttpServerRequest {

    ScxHttpMethod method();

    URIPath path();

    URIQuery query();

    HttpVersion version();

    ScxHttpHeaders headers();

    ScxHttpBody body();

    ScxHttpServerResponse response();

    default String getHeader(ScxHttpHeaderName name) {
        return headers().get(name);
    }

}

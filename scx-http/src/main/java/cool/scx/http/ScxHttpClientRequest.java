package cool.scx.http;

import cool.scx.http.uri.URI;
import cool.scx.http.uri.URIWritable;

public interface ScxHttpClientRequest {

    static ScxHttpClientRequest of() {
        return new ScxHttpClientRequestImpl();
    }

    ScxHttpMethod method();

    URIWritable uri();

    ScxHttpHeadersWritable headers();

    Object body();

    ScxHttpClientRequest method(HttpMethod method);

    ScxHttpClientRequest uri(URIWritable uri);

    ScxHttpClientRequest headers(ScxHttpHeadersWritable headers);

    ScxHttpClientRequest body(Object body);

    default ScxHttpClientRequest uri(String uri) {
        return uri(URI.of(uri));
    }

    default ScxHttpClientRequest setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

}

package cool.scx.http;

import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;

/**
 * ScxHttpClientRequest
 */
public interface ScxHttpClientRequestBuilder {

    ScxHttpMethod method();

    ScxURIWritable uri();

    ScxHttpHeadersWritable headers();

    Object body();

    ScxHttpClientRequestBuilder method(HttpMethod method);

    ScxHttpClientRequestBuilder uri(ScxURIWritable uri);

    ScxHttpClientRequestBuilder headers(ScxHttpHeadersWritable headers);

    ScxHttpClientRequestBuilder body(Object body);

    ScxHttpClientResponse request();

    default ScxHttpClientRequestBuilder uri(String uri) {
        return uri(ScxURI.of(uri));
    }

    default ScxHttpClientRequestBuilder setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

}

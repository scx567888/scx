package cool.scx.http;

import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;

/**
 * ScxHttpClientRequest
 */
public interface ScxHttpClientRequest {

    ScxHttpMethod method();

    ScxURIWritable uri();

    ScxHttpHeadersWritable headers();

    ScxHttpClientRequest method(HttpMethod method);

    ScxHttpClientRequest uri(ScxURIWritable uri);

    ScxHttpClientRequest headers(ScxHttpHeadersWritable headers);

    ScxHttpClientResponse send();
    
    ScxHttpClientResponse send(Object body);

    default ScxHttpClientRequest uri(String uri) {
        return uri(ScxURI.of(uri));
    }

    default ScxHttpClientRequest setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

}

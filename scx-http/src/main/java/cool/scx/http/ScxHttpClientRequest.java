package cool.scx.http;

import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;

//todo 
public interface ScxHttpClientRequest {

    static ScxHttpClientRequest of() {
        return new ScxHttpClientRequestImpl();
    }

    ScxHttpMethod method();

    ScxURIWritable uri();

    ScxHttpHeadersWritable headers();

    Object body();

    ScxHttpClientRequest method(HttpMethod method);

    ScxHttpClientRequest uri(ScxURIWritable uri);

    ScxHttpClientRequest headers(ScxHttpHeadersWritable headers);

    ScxHttpClientRequest body(Object body);

    default ScxHttpClientRequest uri(String uri) {
        return uri(ScxURI.of(uri));
    }

    default ScxHttpClientRequest setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

}

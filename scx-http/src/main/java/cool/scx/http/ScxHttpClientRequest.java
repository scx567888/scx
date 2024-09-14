package cool.scx.http;

public interface ScxHttpClientRequest {

    static ScxHttpClientRequest of() {
        return new ScxHttpClientRequestImpl();
    }

    ScxHttpMethod method();

    URIPath path();

    ScxHttpHeadersWritable headers();

    Object body();

    ScxHttpClientRequest method(HttpMethod method);

    ScxHttpClientRequest path(URIPath path);

    ScxHttpClientRequest body(Object body);

    default ScxHttpClientRequest setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

}

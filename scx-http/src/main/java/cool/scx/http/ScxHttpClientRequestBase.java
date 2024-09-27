package cool.scx.http;

import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;

import static cool.scx.http.HttpMethod.GET;

/**
 * ScxHttpClientRequestBase
 */
public abstract class ScxHttpClientRequestBase implements ScxHttpClientRequest {

    protected HttpMethod method;
    protected ScxURIWritable uri;
    protected ScxHttpHeadersWritable headers;

    public ScxHttpClientRequestBase() {
        this.method = GET;
        this.uri = ScxURI.of();
        this.headers = ScxHttpHeaders.of();
    }

    @Override
    public ScxHttpMethod method() {
        return method;
    }

    @Override
    public ScxURIWritable uri() {
        return uri;
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return headers;
    }

    @Override
    public ScxHttpClientRequest method(HttpMethod method) {
        this.method = method;
        return this;
    }

    @Override
    public ScxHttpClientRequest uri(ScxURIWritable uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public ScxHttpClientRequest headers(ScxHttpHeadersWritable headers) {
        this.headers = headers;
        return this;
    }

}

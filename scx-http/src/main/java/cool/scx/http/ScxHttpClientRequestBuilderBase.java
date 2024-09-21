package cool.scx.http;

import static cool.scx.http.HttpMethod.GET;

/**
 * ScxHttpClientRequestBase
 */
public abstract class ScxHttpClientRequestBuilderBase implements ScxHttpClientRequestBuilder {

    protected HttpMethod method;
    protected ScxURIWritable uri;
    protected ScxHttpHeadersWritable headers;
    protected Object body;

    public ScxHttpClientRequestBuilderBase() {
        this.method = GET;
        this.uri = ScxURI.of();
        this.headers = ScxHttpHeaders.of();
        this.body = null;
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
    public Object body() {
        return body;
    }

    @Override
    public ScxHttpClientRequestBuilder method(HttpMethod method) {
        this.method = method;
        return this;
    }

    @Override
    public ScxHttpClientRequestBuilder uri(ScxURIWritable uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public ScxHttpClientRequestBuilder headers(ScxHttpHeadersWritable headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public ScxHttpClientRequestBuilder body(Object body) {
        this.body = body;
        return this;
    }

}

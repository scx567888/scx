package cool.scx.http;

import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;

import static cool.scx.http.HttpMethod.GET;

//todo 
class ScxHttpClientRequestImpl implements ScxHttpClientRequest {

    private HttpMethod method;
    private ScxURIWritable uri;
    private ScxHttpHeadersWritable headers;
    private Object body;

    public ScxHttpClientRequestImpl() {
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

    @Override
    public ScxHttpClientRequest body(Object body) {
        this.body = body;
        return this;
    }

}

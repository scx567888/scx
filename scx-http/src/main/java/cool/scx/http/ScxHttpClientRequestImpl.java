package cool.scx.http;

import static cool.scx.http.HttpMethod.GET;

public class ScxHttpClientRequestImpl implements ScxHttpClientRequest {

    private ScxHttpMethod method;
    private ScxHttpHeadersWritable headers;
    private URIPath path;
    private Object body;

    public ScxHttpClientRequestImpl() {
        this.method = GET;
        this.path = null;
        this.headers = new ScxHttpHeadersImpl();
    }

    @Override
    public ScxHttpMethod method() {
        return this.method;
    }

    @Override
    public URIPath path() {
        return this.path;
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return this.headers;
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
    public ScxHttpClientRequest path(URIPath path) {
        this.path = path;
        return this;
    }

    @Override
    public ScxHttpClientRequest body(Object body) {
        this.body = body;
        return this;
    }

}

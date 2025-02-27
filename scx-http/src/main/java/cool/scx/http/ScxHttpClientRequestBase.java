package cool.scx.http;

import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;

import static cool.scx.http.HttpMethod.GET;

/// ScxHttpClientRequestBase
///
/// @author scx567888
/// @version 0.0.1
public abstract class ScxHttpClientRequestBase implements ScxHttpClientRequest {

    protected HttpVersion version;
    protected HttpMethod method;
    protected ScxURIWritable uri;
    protected ScxHttpHeadersWritable headers;

    public ScxHttpClientRequestBase() {
        this.version = null;// null 表示自动协商
        this.method = GET;
        this.uri = ScxURI.of();
        this.headers = ScxHttpHeaders.of();
    }

    @Override
    public HttpVersion version() {
        return version;
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
    public ScxHttpClientRequest version(HttpVersion version) {
        this.version = version;
        return this;
    }

    @Override
    public ScxHttpClientRequest method(HttpMethod method) {
        this.method = method;
        return this;
    }

    @Override
    public ScxHttpClientRequest uri(ScxURI uri) {
        this.uri = ScxURI.of(uri);
        return this;
    }

    @Override
    public ScxHttpClientRequest headers(ScxHttpHeaders headers) {
        this.headers = ScxHttpHeaders.of(headers);
        return this;
    }

}

package cool.scx.http.x;

import cool.scx.http.*;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.method.HttpMethod;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.x.http1.Http1ClientConnection;
import cool.scx.http.x.http2.Http2ClientConnection;
import cool.scx.tcp.ScxTCPSocket;

import static cool.scx.http.method.HttpMethod.GET;

/// todo 待完成
///
/// @author scx567888
/// @version 0.0.1
public class XHttpClientRequest implements ScxHttpClientRequest {

    private final XHttpClient httpClient;
    private final XHttpClientOptions options;
    public ScxTCPSocket tcpSocket;

    protected HttpVersion version;
    protected HttpMethod method;
    protected ScxURIWritable uri;
    protected ScxHttpHeadersWritable headers;

    public XHttpClientRequest(XHttpClient httpClient) {
        this.httpClient = httpClient;
        this.options = httpClient.options();
        this.version = null;// null 表示自动协商
        this.method = GET;
        this.uri = ScxURI.of();
        this.headers = ScxHttpHeaders.of();
    }

    @Override
    public ScxHttpClientResponse send(MediaWriter writer) {

        this.tcpSocket = httpClient.createTCPSocket(uri, getApplicationProtocols());

        var useHttp2 = false;

        if (this.tcpSocket.isTLS()) {
            var applicationProtocol = this.tcpSocket.tlsManager().getApplicationProtocol();
            useHttp2 = "h2".equals(applicationProtocol);
        }

        if (useHttp2) {
            return new Http2ClientConnection(tcpSocket, options).sendRequest(this, writer).waitResponse();
        } else {
            return new Http1ClientConnection(tcpSocket, options).sendRequest(this, writer).waitResponse();
        }

    }

    private String[] getApplicationProtocols() {
        if (this.options.enableHttp2()) {
            return new String[]{"http/1.1", "h2"};
        } else {
            return new String[]{"http/1.1"};
        }
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

package cool.scx.http.x;

import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.version.HttpVersion;
import cool.scx.http.x.http1.Http1ClientConnection;
import cool.scx.http.x.http1.Http1ClientRequest;
import cool.scx.http.x.http1.request_line.RequestTargetForm;
import cool.scx.http.x.http2.Http2ClientConnection;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.UncheckedIOException;

import static cool.scx.http.method.HttpMethod.GET;
import static cool.scx.http.version.HttpVersion.HTTP_1_1;
import static cool.scx.http.version.HttpVersion.HTTP_2;
import static cool.scx.http.x.http1.request_line.RequestTargetForm.ABSOLUTE_FORM;
import static cool.scx.http.x.http1.request_line.RequestTargetForm.ORIGIN_FORM;

/// todo 待完成
///
/// @author scx567888
/// @version 0.0.1
public class HttpClientRequest implements Http1ClientRequest {

    private final HttpClient httpClient;
    private final HttpClientOptions options;

    protected HttpVersion version;
    protected ScxHttpMethod method;
    protected ScxURIWritable uri;
    protected ScxHttpHeadersWritable headers;
    protected RequestTargetForm requestTargetForm;

    public HttpClientRequest(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.options = httpClient.options();
        this.version = null;// null 表示自动协商
        this.method = GET;
        this.uri = ScxURI.of();
        this.headers = ScxHttpHeaders.of();
        this.requestTargetForm = ORIGIN_FORM;
    }

    @Override
    public ScxHttpClientResponse send(MediaWriter writer) {

        ScxTCPSocket tcpSocket;

        try {
            tcpSocket = httpClient.createTCPSocket(uri, getApplicationProtocols());
        } catch (IOException e) {
            throw new UncheckedIOException("创建连接失败 !!!", e);
        }

        var useHttp2 = false;

        if (tcpSocket.isTLS()) {
            var applicationProtocol = tcpSocket.tlsManager().getApplicationProtocol();
            useHttp2 = "h2".equals(applicationProtocol);
        }

        if (useHttp2) {
            return new Http2ClientConnection(tcpSocket, options).sendRequest(this, writer).waitResponse();
        } else {
            //仅当 http 协议并且开启代理的时候才使用 绝对路径
            if (!tcpSocket.isTLS() && options.proxy() != null && options.proxy().enabled()) {
                this.requestTargetForm = ABSOLUTE_FORM;
            }
            return new Http1ClientConnection(tcpSocket, options).sendRequest(this, writer).waitResponse();
        }

    }

    private String[] getApplicationProtocols() {
        if (this.options.enableHttp2()) {
            return new String[]{HTTP_1_1.alpnValue(), HTTP_2.alpnValue()};
        } else {
            return new String[]{HTTP_1_1.alpnValue()};
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
    public ScxHttpClientRequest method(ScxHttpMethod method) {
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

    @Override
    public RequestTargetForm requestTargetForm() {
        return requestTargetForm;
    }

    @Override
    public HttpClientRequest requestTargetForm(RequestTargetForm requestTargetForm) {
        this.requestTargetForm = requestTargetForm;
        return this;
    }

}

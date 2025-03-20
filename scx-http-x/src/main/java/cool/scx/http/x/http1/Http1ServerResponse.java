package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.status.HttpStatus;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.x.http1.chunked.HttpChunkedOutputStream;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.status_line.Http1StatusLine;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import static cool.scx.http.headers.ScxHttpHeadersHelper.encodeHeaders;
import static cool.scx.http.status.ScxHttpStatusHelper.getReasonPhrase;
import static cool.scx.http.x.http1.Http1Helper.checkIsChunkedTransfer;
import static cool.scx.http.x.http1.Http1Helper.checkResponseHasBody;
import static cool.scx.http.x.http1.headers.connection.ConnectionType.CLOSE;
import static cool.scx.http.x.http1.headers.connection.ConnectionType.KEEP_ALIVE;
import static cool.scx.http.x.http1.headers.transfer_encoding.EncodingType.CHUNKED;
import static java.io.OutputStream.nullOutputStream;

/// todo 待完成
///
/// @author scx567888
/// @version 0.0.1
public class Http1ServerResponse implements ScxHttpServerResponse {

    private final Http1ServerRequest request;
    private final Http1Headers headers;
    private final OutputStream dataWriter;
    private ScxHttpStatus status;
    private String reasonPhrase;
    private OutputStream outputStream;

    Http1ServerResponse(Http1ServerConnection connection, Http1ServerRequest request) {
        this.request = request;
        this.status = HttpStatus.OK;
        this.headers = new Http1Headers();
        this.dataWriter = new Http1ServerResponseOutputStream(connection, this.headers);
    }

    @Override
    public ScxHttpServerRequest request() {
        return request;
    }

    @Override
    public ScxHttpStatus status() {
        return status;
    }

    public String reasonPhrase() {
        return reasonPhrase;
    }

    public Http1ServerResponse reasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
        return this;
    }

    @Override
    public Http1Headers headers() {
        return headers;
    }

    @Override
    public ScxHttpServerResponse status(ScxHttpStatus code) {
        status = code;
        return this;
    }

    @Override
    public OutputStream outputStream() {
        if (outputStream == null) {
            outputStream = sendHeaders();
        }
        return outputStream;
    }

    @Override
    public boolean isClosed() {
        //todo 这里的 isClosed 应该表示 什么 是当前 响应已结束 还是 当前连接已结束
        return false;
    }

    public String createReasonPhrase() {
        return reasonPhrase != null ? reasonPhrase : getReasonPhrase(status, "unknown");
    }

    private OutputStream sendHeaders() {

        //1, 响应行
        var statusLine = new Http1StatusLine(request.version(), status.code(), createReasonPhrase());

        var sb = new StringBuilder(statusLine.encode()).append("\r\n");

        //用户可能已经自行设置了 CONNECTION
        if (headers.connection() == null) {
            if (request.isKeepAlive) {
                headers.connection(KEEP_ALIVE);
            } else {
                headers.connection(CLOSE);
            }
        }

        var hasBody = checkResponseHasBody(status);

        //如果需要响应体
        if (hasBody) {
            //没有设置 contentLength 我们帮助设置 
            if (headers.contentLength() == null) {
                headers.transferEncoding(CHUNKED);
            }
        }

        var useChunkedTransfer = checkIsChunkedTransfer(headers);

        //判断是否需要分段传输
        var headerStr = encodeHeaders(headers);

        sb.append(headerStr);
        sb.append("\r\n");

        try {
            dataWriter.write(sb.toString().getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        //没有响应体
        if (!hasBody) {
            return nullOutputStream();
        }

        //采用分块传输
        if (useChunkedTransfer) {
            return new HttpChunkedOutputStream(dataWriter);
        } else {
            //直接返回原始格式
            return dataWriter;
        }

    }

}

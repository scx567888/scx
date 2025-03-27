package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.status.HttpStatus;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.x.http1.chunked.HttpChunkedOutputStream;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.status_line.Http1StatusLine;
import cool.scx.io.io_stream.CheckedOutputStream;
import cool.scx.io.io_stream.NullCheckedOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import static cool.scx.http.headers.ScxHttpHeadersHelper.encodeHeaders;
import static cool.scx.http.status.ScxHttpStatusHelper.getReasonPhrase;
import static cool.scx.http.x.http1.Http1Helper.checkResponseHasBody;
import static cool.scx.http.x.http1.headers.connection.Connection.CLOSE;
import static cool.scx.http.x.http1.headers.connection.Connection.KEEP_ALIVE;
import static cool.scx.http.x.http1.headers.transfer_encoding.TransferEncoding.CHUNKED;

/// Http1ServerResponse
///
/// @author scx567888
/// @version 0.0.1
public class Http1ServerResponse implements ScxHttpServerResponse {

    protected final Http1ServerConnection connection;
    protected final Http1ServerRequest request;
    protected final Http1Headers headers;
    protected ScxHttpStatus status;
    protected String reasonPhrase;
    protected OutputStream outputStream;

    Http1ServerResponse(Http1ServerConnection connection, Http1ServerRequest request) {
        this.connection = connection;
        this.request = request;
        this.status = HttpStatus.OK;
        this.headers = new Http1Headers();
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
    public OutputStream outputStream(long expectedLength) {
        if (outputStream == null) {
            outputStream = sendHeaders();
        }
        return outputStream;
    }

    @Override
    public boolean isSent() {
        if (outputStream == null) {
            return false;
        }
        var o = outputStream instanceof HttpChunkedOutputStream c ? c.outputStream() : outputStream;
        if (o instanceof CheckedOutputStream c) {
            return c.isClosed();
        }
        throw new IllegalStateException("unknown type output stream");
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
            if (request.isKeepAlive()) {
                // 正常我们可以忽略设置 KEEP_ALIVE, 但是这里我们显式设置
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

        var useChunkedTransfer = headers.transferEncoding() == CHUNKED;

        //判断是否需要分段传输
        var headerStr = encodeHeaders(headers);

        sb.append(headerStr);
        sb.append("\r\n");

        try {
            connection.dataWriter.write(sb.toString().getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        //3, 只有明确表示 close 的时候我们才关闭
        var closeConnection = headers.connection() == CLOSE;

        //没有响应体
        if (!hasBody) {
            return new NullCheckedOutputStream();
        }

        // todo 这里的 Http1ServerResponseOutputStream 应该根据 contentLength 进行限制
        var baseOutputStream = new Http1ServerResponseOutputStream(connection, closeConnection);
        //采用分块传输
        return useChunkedTransfer ? new HttpChunkedOutputStream(baseOutputStream) : baseOutputStream;

    }

}

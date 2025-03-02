package cool.scx.http.x.http1x;

import cool.scx.http.*;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.x.http1x.Http1xHelper.CRLF_BYTES;
import static cool.scx.http.x.http1x.Http1xHelper.sendChunkedEnd;

/// todo 待完成
///
/// @author scx567888
/// @version 0.0.1
public class Http1xServerResponse extends OutputStream implements ScxHttpServerResponse {

    private final Http1xServerConnection connection;
    private final Http1xServerRequest request;
    private final ScxHttpHeadersWritable headers;
    private final OutputStream dataWriter;
    private HttpStatusCode status;
    private boolean firstSend;
    private boolean useChunkedTransfer;

    Http1xServerResponse(Http1xServerConnection connection, Http1xServerRequest request) {
        this.connection = connection;
        this.dataWriter = this.connection.dataWriter;
        this.request = request;
        this.status = HttpStatusCode.OK;
        this.headers = ScxHttpHeaders.of();
        this.firstSend = true;
        this.useChunkedTransfer = false;
    }

    @Override
    public ScxHttpServerRequest request() {
        return request;
    }

    @Override
    public HttpStatusCode status() {
        return status;
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return headers;
    }

    @Override
    public ScxHttpServerResponse status(HttpStatusCode code) {
        status = code;
        return this;
    }

    @Override
    public OutputStream outputStream() {
        return this;
    }

    @Override
    public void end() {
        //分块传输别忘了最后的 终结块
        if (useChunkedTransfer) {
            try {
                sendChunkedEnd(dataWriter);
            } catch (Exception e) {
                throw new CloseConnectionException();
            }
        }
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    public void doFirstSend() {
        var sb = new StringBuilder();
        sb.append(request.version().value());
        sb.append(" ");
        sb.append(status.code());
        sb.append(" ");
        sb.append(status.description());
        sb.append("\r\n");

        //用户可能已经自行设置了 CONNECTION
        if (!headers.contains(CONNECTION)) {
            if (request.isKeepAlive) {
                headers.set(CONNECTION, "keep-alive");
            } else {
                headers.set(CONNECTION, "close");
            }
        }

        //用户可能已经自行设置了 SERVER
        if (!headers.contains(SERVER)) {
            headers.set(SERVER, "SCX");
        }

        //这里 如果用户没有设置相应长度 我们采用 默认 分块传输
        if (headers.contentLength() == null) {
            //不是所有响应都有响应体 一些不需要响应体的响应 我们不启用分块传输
            if (status != HttpStatusCode.SWITCHING_PROTOCOLS &&
                    status != HttpStatusCode.NO_CONTENT &&
                    status != HttpStatusCode.NOT_MODIFIED) {
                headers.set(TRANSFER_ENCODING, "chunked");
                this.useChunkedTransfer = true;
            }
        }

        var headerStr = headers.encode();

        sb.append(headerStr);
        sb.append("\r\n");

        try {
            dataWriter.write(sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void checkFirstSend() {
        if (firstSend) {
            doFirstSend();
            firstSend = false;
        }
    }

    @Override
    public void write(int b) throws IOException {
        var a = new byte[]{(byte) b};
        write(a, 0, 1);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        checkFirstSend();

        if (useChunkedTransfer) {
            // 发送分块
            dataWriter.write(Integer.toUnsignedString(len, 16).getBytes());  // 发送块大小
            dataWriter.write(CRLF_BYTES);  // 块大小结束
            dataWriter.write(b, off, len);  // 发送数据块内容
            dataWriter.write(CRLF_BYTES);  // 分块结束符
        } else {
            dataWriter.write(b, off, len);
        }

    }

    @Override
    public void flush() throws IOException {
        dataWriter.flush();
    }

    @Override
    public void close() throws IOException {
        checkFirstSend();
        var connection = headers.get(CONNECTION);
        //只有明确表示 close 的时候我们才关闭
        if ("close".equalsIgnoreCase(connection)) {
            this.connection.stop();
        }
    }

}

package cool.scx.http.x.http1;

import cool.scx.http.*;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpStatusCode.*;
import static cool.scx.http.x.http1.Http1Helper.*;

/// todo 待完成
///
/// @author scx567888
/// @version 0.0.1
public class Http1ServerResponse extends OutputStream implements ScxHttpServerResponse {

    private final Http1ServerConnection connection;
    private final Http1ServerRequest request;
    private final ScxHttpHeadersWritable headers;
    private final OutputStream dataWriter;
    private HttpStatusCode status;
    private boolean firstSend;
    private boolean useChunkedTransfer;
    private boolean hasBody;

    Http1ServerResponse(Http1ServerConnection connection, Http1ServerRequest request) {
        this.connection = connection;
        this.dataWriter = this.connection.dataWriter;
        this.request = request;
        this.status = HttpStatusCode.OK;
        this.headers = ScxHttpHeaders.of();
        this.firstSend = true;
        this.useChunkedTransfer = false;
        this.hasBody = true;
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
    public boolean isClosed() {
        //todo 这里的 isClosed 应该表示 什么 是当前 响应已结束 还是 当前连接已结束
        return false;
    }

    public void doFirstSend() {
        //1, 响应头
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

        //是否不需要响应体
        if (status == SWITCHING_PROTOCOLS || status == NO_CONTENT || status == NOT_MODIFIED) {
            this.hasBody = false;
        }

        //如果需要响应体
        if (this.hasBody) {
            //没有设置 contentLength 我们帮助设置 
            if (headers.contentLength() == null) {
                headers.set(TRANSFER_ENCODING, "chunked");
            }
        }

        //判断是否需要分段传输
        if (checkIsChunkedTransfer(headers)) {
            this.useChunkedTransfer = true;
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
        //1, 有可能从来没有调用过 write , 这里需要检查一下
        checkFirstSend();
        //2, 分块传输别忘了最后的 终结块
        if (hasBody && useChunkedTransfer) {
            try {
                sendChunkedEnd(dataWriter);
            } catch (IOException e) {
                throw new CloseConnectionException();
            }
        }
        //3, 只有明确表示 close 的时候我们才关闭
        var connection = headers.get(CONNECTION);
        if ("close".equalsIgnoreCase(connection)) {
            this.connection.close();// 服务器也需要显式关闭连接
        }
    }

}

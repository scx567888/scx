package cool.scx.http.usagi.http1x;

import cool.scx.http.*;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.HttpFieldName.CONNECTION;
import static cool.scx.http.HttpFieldName.SERVER;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xServerResponse extends OutputStream implements ScxHttpServerResponse {

    private final Http1xServerConnection connection;
    private final Http1xServerRequest request;
    private final ScxHttpHeadersWritable headers;
    private final OutputStream dataWriter;
    private HttpStatusCode status;
    private boolean firstSend;

    Http1xServerResponse(Http1xServerConnection connection, Http1xServerRequest request) {
        this.connection = connection;
        this.dataWriter = this.connection.dataWriter;
        this.request = request;
        this.status = HttpStatusCode.OK;
        this.headers = ScxHttpHeaders.of();
        this.firstSend = true;
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
        checkFirstSend();
        dataWriter.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        checkFirstSend();
        dataWriter.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        checkFirstSend();
        dataWriter.write(b, off, len);
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

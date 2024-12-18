package cool.scx.http.usagi.http1x;

import cool.scx.http.*;
import cool.scx.io.LinkedDataReader;
import cool.scx.tcp.ScxTCPSocket;

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

    private final ScxTCPSocket tcpSocket;
    private final LinkedDataReader dataReader;
    private final OutputStream dataWriter;

    private final Http1xServerRequest request;
    private final ScxHttpHeadersWritable headers;
    private final boolean isKeepAlive;
    private HttpStatusCode status;
    private boolean firstSend;

    Http1xServerResponse(Http1xServerRequest request, ScxTCPSocket tcpSocket, LinkedDataReader dataReader, OutputStream dataWriter, boolean isKeepAlive) {
        this.tcpSocket = tcpSocket;
        this.dataReader = dataReader;
        this.dataWriter = dataWriter;
        this.isKeepAlive = isKeepAlive;

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

        if (isKeepAlive) {
            //用户可能已经自行设置了 CONNECTION
            if (!headers.contains(CONNECTION)) {
                headers.set(CONNECTION, "keep-alive");
            }
        } else {
            headers.set(CONNECTION, "close");
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
    }

}

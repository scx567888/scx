package cool.scx.http.usagi;

import cool.scx.http.*;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.OutputStream;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpServerResponse implements ScxHttpServerResponse {

    private final UsagiHttpServerRequest request;
    private final ScxTCPSocket tcpSocket;
    private final OutputStream outputStream;
    private final ScxHttpHeadersWritable headers;
    private final OutputStream out;
    private HttpStatusCode status;
    private boolean firstSend;

    UsagiHttpServerResponse(UsagiHttpServerRequest request, ScxTCPSocket tcpSocket) {
        this.request = request;
        this.tcpSocket = tcpSocket;
        this.out = tcpSocket.outputStream();
        this.headers = ScxHttpHeaders.of();
        this.firstSend = true;
        this.status = HttpStatusCode.OK;
        this.outputStream = new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                checkFirstSend();
                out.write(b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                checkFirstSend();
                out.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                checkFirstSend();
                out.write(b, off, len);
            }

            @Override
            public void flush() throws IOException {
                out.flush();
            }

            @Override
            public void close() throws IOException {
                checkFirstSend();
            }

        };
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
        return outputStream;
    }

    @Override
    public void end() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    public void checkFirstSend() {
        if (firstSend) {
            doFirstSend();
            firstSend = false;
        }
    }

    public void doFirstSend() {
        var sb = new StringBuilder();
        sb.append(request.version.value());
        sb.append(" ");
        sb.append(status.code());
        sb.append(" ");
        sb.append(status.description());
        sb.append("\r\n");

        var headerStr = headers.encode();

        sb.append(headerStr);
        sb.append("\r\n");

        try {
            out.write(sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

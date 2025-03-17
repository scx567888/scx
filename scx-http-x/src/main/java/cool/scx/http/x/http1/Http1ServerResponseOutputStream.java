package cool.scx.http.x.http1;

import cool.scx.http.headers.ScxHttpHeaders;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.headers.connection.ConnectionType.CLOSE;

public class Http1ServerResponseOutputStream extends OutputStream {

    private final Http1ServerConnection connection;
    private final ScxHttpHeaders headers;
    private final OutputStream out;

    public Http1ServerResponseOutputStream(Http1ServerConnection connection, ScxHttpHeaders headers) {
        this.connection = connection;
        this.headers = headers;
        this.out = connection.dataWriter;
    }

    @Override
    public void write(int b) throws IOException {
        this.out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.out.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public void close() throws IOException {
        //3, 只有明确表示 close 的时候我们才关闭
        var connection = headers.connection();
        if (connection.contains(CLOSE)) {
            this.connection.close();// 服务器也需要显式关闭连接
        }
    }

}

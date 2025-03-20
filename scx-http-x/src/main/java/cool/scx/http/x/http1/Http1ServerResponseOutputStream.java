package cool.scx.http.x.http1;

import cool.scx.io.io_stream.CheckedOutputStream;

import java.io.IOException;

public class Http1ServerResponseOutputStream extends CheckedOutputStream {

    private final Http1ServerConnection connection;
    private final boolean closeConnection;

    public Http1ServerResponseOutputStream(Http1ServerConnection connection, boolean closeConnection) {
        this.connection = connection;
        this.closeConnection = closeConnection;
    }

    @Override
    public void write(int b) throws IOException {
        ensureOpen();
        connection.dataWriter.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        ensureOpen();
        connection.dataWriter.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        ensureOpen();
        connection.dataWriter.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        ensureOpen();
        connection.dataWriter.flush();
    }

    @Override
    public void close() throws IOException {
        closed = true;
        //3, 只有明确表示 close 的时候我们才关闭
        if (closeConnection) {
            this.connection.close();// 服务器也需要显式关闭连接
        }
    }

}

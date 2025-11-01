package cool.scx.http.x.http1;

import cool.scx.io.ByteOutput;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;

import java.io.IOException;

public class Http1ServerResponseOutputStream implements ByteOutput {

    private volatile boolean closed;
    private final Http1ServerConnection connection;
    private final boolean closeConnection;

    public Http1ServerResponseOutputStream(Http1ServerConnection connection, boolean closeConnection) {
        this.connection = connection;
        this.closeConnection = closeConnection;
        this.closed = false;
    }

    private void ensureOpen() throws AlreadyClosedException {
        if (closed) {
            throw new AlreadyClosedException();
        }
    }

    @Override
    public void write(byte b)  {
        ensureOpen();
        connection.dataWriter.write(b);
    }

    @Override
    public void write(byte[] b)  {
        ensureOpen();
        connection.dataWriter.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len)  {
        ensureOpen();
        connection.dataWriter.write(b, off, len);
    }

    @Override
    public void flush()  {
        ensureOpen();
        connection.dataWriter.flush();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close()  {
        closed = true;
        //3, 只有明确表示 close 的时候我们才关闭
        if (closeConnection) {
            try {
                this.connection.close();// 服务器也需要显式关闭连接
            } catch (IOException e) {
                throw new ScxIOException(e);
            }
        }
    }

}

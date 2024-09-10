package cool.scx.http_server.impl.scx;

import cool.scx.http_server.ScxHttpResponse;
import cool.scx.http_server.ScxTCPSocket;
import io.vertx.core.Future;

import java.io.File;
import java.io.IOException;

public class ScxHttpResponseImpl implements ScxHttpResponse {

    private final ScxTCPSocket tcpSocket;

    public ScxHttpResponseImpl(ScxTCPSocket tcpSocket) {
        this.tcpSocket = tcpSocket;
    }

    @Override
    public void write(byte[] bytes) {
        try {
            this.tcpSocket.getOutputStream().write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(File file) {

    }

    @Override
    public Future<Void> sendFile(File filename, long offset, long length) {
        return null;
    }

    @Override
    public void end(byte[] bytes) {
        write(bytes);
        end();
    }

    @Override
    public void end(File file) {
        write(file);
        end();
    }

    @Override
    public void end() {
        try {
            this.tcpSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

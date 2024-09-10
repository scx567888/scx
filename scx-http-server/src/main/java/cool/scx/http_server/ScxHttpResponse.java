package cool.scx.http_server;

import io.vertx.core.Future;

import java.io.File;

public interface ScxHttpResponse {

    void write(byte[] bytes);

    void write(File file);
    
    default Future<Void> sendFile(File file) {
        return sendFile(file, 0);
    }
    
    default Future<Void> sendFile(File file, long offset) {
        return sendFile(file, offset, Long.MAX_VALUE);
    }
    
    Future<Void> sendFile(File filename, long offset, long length);

    void end(byte[] bytes);

    void end(File file);

    void end();

}

package cool.scx.http_server;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;

public interface ScxHttpRequest {

    ScxHttpResponse response();

    byte[] body();
    
    Future<Buffer> body1();
    
}

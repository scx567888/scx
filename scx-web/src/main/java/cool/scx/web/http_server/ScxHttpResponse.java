package cool.scx.web.http_server;

import java.io.File;

public interface ScxHttpResponse {

    void write(byte[] bytes);

    void write(File file);

    void end(byte[] bytes);

    void end(File file);

    void end();

}

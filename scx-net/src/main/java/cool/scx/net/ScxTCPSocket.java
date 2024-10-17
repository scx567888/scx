package cool.scx.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Path;

public interface ScxTCPSocket {

    Socket socket();

    InputStream inputStream();

    OutputStream outputStream();

    void sendFile(Path path, long offset, long length) throws IOException;

}

package cool.scx.net;

import java.io.IOException;
import java.nio.file.Path;

public interface ScxTCPSocket {

    byte[] read(int maxLength) throws IOException;

    void write(byte[] bytes) throws IOException;

    void sendFile(Path path, long offset, long length) throws IOException;

}

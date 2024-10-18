package cool.scx.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public interface DataChannel {

    void write(ByteBuffer buffer) throws IOException;

    void write(byte[] bytes, int offset, int length) throws IOException;

    void write(byte[] bytes) throws IOException;

    void write(Path path, long offset, long length) throws IOException;

    void write(Path path) throws IOException;

    int read(ByteBuffer buffer) throws IOException;

    int read(byte[] bytes, int offset, int length) throws IOException;

    int read(byte[] bytes) throws IOException;

    void read(Path path, long offset, long length) throws IOException;

    void read(Path path) throws IOException;

    byte[] read(int maxLength) throws IOException, NoMoreDataException;

    void close() throws IOException;

    boolean isOpen() throws IOException;

}

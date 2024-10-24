package cool.scx.net;

import cool.scx.io.NoMoreDataException;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * ScxTCPSocket
 */
public interface ScxTCPSocket {

    void write(ByteBuffer buffer) throws IOException;

    void write(byte[] bytes, int offset, int length) throws IOException;

    void write(byte[] bytes) throws IOException;

    void write(Path path, long offset, long length) throws IOException;

    void write(Path path) throws IOException;

    int read(ByteBuffer buffer) throws IOException;

    int read(byte[] bytes, int offset, int length) throws IOException;

    int read(byte[] bytes) throws IOException;

    void read(Path path, long offset, long length, OpenOption... options) throws IOException;

    void read(Path path, OpenOption... options) throws IOException;

    /**
     * 读取字节
     * 当没有更多的数据时会抛出异常
     *
     * @param maxLength 最大长度
     * @return 读取的字节
     * @throws IOException         异常
     * @throws NoMoreDataException 没有更多数据时抛出
     */
    byte[] read(int maxLength) throws IOException, NoMoreDataException;

    void close() throws IOException;

    boolean isOpen() throws IOException;

    SocketAddress remoteAddress() throws IOException;

}

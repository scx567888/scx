package cool.scx.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * 数据通道接口，提供读写数据的方法
 */
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

}

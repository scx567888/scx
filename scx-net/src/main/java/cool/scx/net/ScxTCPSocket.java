package cool.scx.net;

import cool.scx.io.NoMoreDataException;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public interface ScxTCPSocket {

    void write(ByteBuffer buffer) throws IOException;

    void write(byte[] bytes, int offset, int length) throws IOException;

    void write(byte[] bytes) throws IOException;

    void write(Path path, long offset, long length) throws IOException;

    void write(Path path) throws IOException;

    int read(ByteBuffer buffer) throws IOException, NoMoreDataException;

    int read(byte[] bytes, int offset, int length) throws IOException, NoMoreDataException;

    int read(byte[] bytes) throws IOException, NoMoreDataException;

    int read(Path path, long offset, long length) throws IOException;

    int read(Path path) throws IOException;

    /**
     * 关闭 Socket
     *
     * @throws IOException Socket IO 异常
     */
    void close() throws IOException;

    /**
     * 获取远程地址
     *
     * @return 远程地址
     * @throws IOException Socket IO 异常
     */
    SocketAddress getRemoteAddress() throws IOException;

}

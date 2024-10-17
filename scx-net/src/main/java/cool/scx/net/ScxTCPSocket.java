package cool.scx.net;

import cool.scx.io.NoMoreDataException;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.file.Path;

public interface ScxTCPSocket {

    /**
     * 读取字节
     * 当没有更多的数据时会抛出异常
     *
     * @param maxLength 最大长度
     * @return bytes
     * @throws IOException         Socket IO 异常
     * @throws NoMoreDataException 没有更多数据时抛出
     */
    byte[] read(int maxLength) throws IOException, NoMoreDataException;

    /**
     * 写入字节
     *
     * @param bytes 数据
     * @throws IOException Socket IO 异常
     */
    void write(byte[] bytes) throws IOException;

    /**
     * 发送文件
     *
     * @param path   文件路径
     * @param offset 起始位置
     * @param length 长度
     * @throws IOException Socket IO 异常
     */
    void sendFile(Path path, long offset, long length) throws IOException;

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

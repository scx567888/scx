package cool.scx.io;

import java.io.OutputStream;

public interface DataReader {

    byte[] EMPTY_BYTES = new byte[0];

    /**
     * 读取单个字节 (指针会移动)
     * 当没有更多的数据时会抛出异常
     *
     * @return byte
     * @throws NoMoreDataException 没有更多数据时抛出
     */
    byte read() throws NoMoreDataException;

    /**
     * 读取字节 (指针会移动)
     * 当没有更多的数据时会抛出异常
     *
     * @param maxLength 最大长度
     * @return bytes
     * @throws NoMoreDataException 没有更多数据时抛出
     */
    byte[] read(int maxLength) throws NoMoreDataException;

    /**
     * 向 outputStream 写入指定长度字节 (指针会移动)
     * 当没有更多的数据时会抛出异常
     *
     * @param maxLength 最大长度
     * @throws NoMoreDataException 没有更多数据时抛出
     */
    void read(OutputStream outputStream, int maxLength) throws NoMoreDataException;

    /**
     * 读取单个字节 (指针不会移动)
     * 当没有更多的数据时会抛出异常
     *
     * @return byte
     * @throws NoMoreDataException 没有更多数据时抛出
     */
    byte peek() throws NoMoreDataException;

    /**
     * 读取指定长度字节 (指针不会移动)
     * 当没有更多的数据时会抛出异常
     *
     * @param maxLength 最大长度
     * @return byte
     * @throws NoMoreDataException 没有更多数据时抛出
     */
    byte[] peek(int maxLength) throws NoMoreDataException;

    /**
     * 向 outputStream 写入指定长度字节 (指针不会移动)
     * 当没有更多的数据时会抛出异常
     *
     * @param maxLength 最大长度
     * @throws NoMoreDataException 没有更多数据时抛出
     */
    void peek(OutputStream outputStream, int maxLength) throws NoMoreDataException;

    /**
     * 查找 指定字节 第一次出现的 index (指针不会移动)
     *
     * @param b 指定字节
     * @return index 或者 -1 (未找到)
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    int indexOf(byte b) throws NoMatchFoundException;

    /**
     * 查找 指定字节数组 第一次出现的 index (指针不会移动)
     *
     * @param b 指定字节数组
     * @return index 或者 -1 (未找到)
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    int indexOf(byte[] b) throws NoMatchFoundException;

    /**
     * 向后移动指定字节
     *
     * @param length 移动长度
     */
    void skip(int length);

    /**
     * 读取 直到 找到匹配的字节 (指针会移动)
     *
     * @param b 指定字节
     * @return bytes
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default byte[] readUntil(byte b) throws NoMatchFoundException {
        var index = indexOf(b);
        var data = read(index);
        skip(1);
        return data;
    }

    /**
     * 读取 直到 找到匹配的字节 (指针会移动)
     *
     * @param b 指定字节
     * @return bytes
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default byte[] readUntil(byte[] b) throws NoMatchFoundException {
        var index = indexOf(b);
        var data = read(index);
        skip(b.length);
        return data;
    }

    /**
     * 读取 直到 找到匹配的字节 (指针不会移动)
     *
     * @param b 指定字节
     * @return bytes
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default byte[] peekUntil(byte b) throws NoMatchFoundException {
        var index = indexOf(b);
        return peek(index);
    }

    /**
     * 读取 直到 找到匹配的字节 (指针不会移动)
     *
     * @param b 指定字节
     * @return bytes
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default byte[] peekUntil(byte[] b) throws NoMatchFoundException {
        var index = indexOf(b);
        return peek(index);
    }

}

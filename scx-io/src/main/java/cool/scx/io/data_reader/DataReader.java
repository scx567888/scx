package cool.scx.io.data_reader;

import cool.scx.io.data_consumer.DataConsumer;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;

/**
 * DataReader
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface DataReader {

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
     * 向 dataConsumer 写入指定长度字节 (指针会移动)
     * 当没有更多的数据时会抛出异常
     *
     * @param maxLength 最大长度
     * @throws NoMoreDataException 没有更多数据时抛出
     */
    void read(DataConsumer dataConsumer, int maxLength) throws NoMoreDataException;

    /**
     * 向 dataConsumer 写入指定长度字节 (指针不会移动)
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
    void peek(DataConsumer dataConsumer, int maxLength) throws NoMoreDataException;

    /**
     * 查找 指定字节 第一次出现的 index (指针不会移动)
     *
     * @param b   指定字节
     * @param max 最大长度
     * @return index 或者 -1 (未找到)
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    int indexOf(byte b, int max) throws NoMatchFoundException, NoMoreDataException;

    /**
     * 查找 指定字节数组 第一次出现的 index (指针不会移动)
     *
     * @param b   指定字节数组
     * @param max 最大长度
     * @return index 或者 -1 (未找到)
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    int indexOf(byte[] b, int max) throws NoMatchFoundException, NoMoreDataException;

    /**
     * 向后移动指定字节
     *
     * @param length 移动长度
     */
    void skip(int length) throws NoMoreDataException;

    /**
     * 查找 指定字节 第一次出现的 index (指针不会移动)
     *
     * @param b 指定字节
     * @return index 或者 -1 (未找到)
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default int indexOf(byte b) throws NoMatchFoundException, NoMoreDataException {
        return indexOf(b, Integer.MAX_VALUE);
    }

    /**
     * 查找 指定字节数组 第一次出现的 index (指针不会移动)
     *
     * @param b 指定字节数组
     * @return index 或者 -1 (未找到)
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default int indexOf(byte[] b) throws NoMatchFoundException, NoMoreDataException {
        return indexOf(b, Integer.MAX_VALUE);
    }

    /**
     * 读取 直到 找到匹配的字节 (指针会移动)
     *
     * @param b 指定字节
     * @return bytes
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default byte[] readUntil(byte b, int max) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(b, max);
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
    default byte[] readUntil(byte[] b, int max) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(b, max);
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
    default byte[] peekUntil(byte b, int max) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(b, max);
        return peek(index);
    }

    /**
     * 读取 直到 找到匹配的字节 (指针不会移动)
     *
     * @param b 指定字节
     * @return bytes
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default byte[] peekUntil(byte[] b, int max) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(b, max);
        return peek(index);
    }

    /**
     * 读取 直到 找到匹配的字节 (指针会移动)
     *
     * @param b 指定字节
     * @return bytes
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default byte[] readUntil(byte b) throws NoMatchFoundException, NoMoreDataException {
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
    default byte[] readUntil(byte[] b) throws NoMatchFoundException, NoMoreDataException {
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
    default byte[] peekUntil(byte b) throws NoMatchFoundException, NoMoreDataException {
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
    default byte[] peekUntil(byte[] b) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(b);
        return peek(index);
    }

}
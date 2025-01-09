package cool.scx.io;

import static java.lang.Math.toIntExact;

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
    void read(DataConsumer dataConsumer, long maxLength) throws NoMoreDataException;

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
    void peek(DataConsumer dataConsumer, long maxLength) throws NoMoreDataException;

    /**
     * 查找 指定字节 第一次出现的 index (指针不会移动)
     *
     * @param b   指定字节
     * @param maxLength 最大长度
     * @return index 或者 -1 (未找到)
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    long indexOf(byte b, long maxLength) throws NoMatchFoundException, NoMoreDataException;

    /**
     * 查找 指定字节数组 第一次出现的 index (指针不会移动)
     *
     * @param b   指定字节数组
     * @param maxLength 最大长度
     * @return index 或者 -1 (未找到)
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    long indexOf(byte[] b, long maxLength) throws NoMatchFoundException, NoMoreDataException;

    /**
     * 向后移动指定字节
     *
     * @param length 移动长度
     */
    void skip(long length) throws NoMoreDataException;

    /**
     * 查找 指定字节 第一次出现的 index (指针不会移动)
     *
     * @param b 指定字节
     * @return index 或者 -1 (未找到)
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default long indexOf(byte b) throws NoMatchFoundException, NoMoreDataException {
        return indexOf(b, Long.MAX_VALUE);
    }

    /**
     * 查找 指定字节数组 第一次出现的 index (指针不会移动)
     *
     * @param b 指定字节数组
     * @return index 或者 -1 (未找到)
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default long indexOf(byte[] b) throws NoMatchFoundException, NoMoreDataException {
        return indexOf(b, Long.MAX_VALUE);
    }

    /**
     * 读取 直到 找到匹配的字节 (指针会移动)
     *
     * @param b 指定字节
     * @return bytes
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default byte[] readUntil(byte b, int maxLength) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(b, maxLength);
        var data = read(toIntExact(index));
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
    default byte[] readUntil(byte[] b, int maxLength) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(b, maxLength);
        var data = read(toIntExact(index));
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
    default byte[] peekUntil(byte b, int maxLength) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(b, maxLength);
        return peek(toIntExact(index));
    }

    /**
     * 读取 直到 找到匹配的字节 (指针不会移动)
     *
     * @param b 指定字节
     * @return bytes
     * @throws NoMatchFoundException 没有匹配时抛出
     */
    default byte[] peekUntil(byte[] b, int maxLength) throws NoMatchFoundException, NoMoreDataException {
        var index = indexOf(b, maxLength);
        return peek(toIntExact(index));
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
        var data = read(toIntExact(index));
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
        var data = read(toIntExact(index));
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
        return peek(toIntExact(index));
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
        return peek(toIntExact(index));
    }

}

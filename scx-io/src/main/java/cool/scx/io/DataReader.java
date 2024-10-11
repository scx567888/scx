package cool.scx.io;

public interface DataReader {

    byte[] EMPTY_BYTES = new byte[0];

    /**
     * 读取单个字节 (指针会移动)
     *
     * @return byte
     */
    byte read();

    /**
     * 读取指定长度字节 (指针会移动)
     *
     * @param length 长度
     * @return bytes
     */
    byte[] read(int length);

    /**
     * 读取单个字节 (指针不会移动)
     *
     * @return byte
     */
    byte get();

    /**
     * 读取指定长度字节 (指针不会移动)
     *
     * @return byte
     */
    byte[] get(int length);

    /**
     * 查找 指定字节 第一次出现的 index (指针不会移动)
     *
     * @param b 指定字节
     * @return index 或者 -1 (未找到)
     */
    int find(byte b);

    /**
     * 查找 指定字节数组 第一次出现的 index (指针不会移动)
     *
     * @param b 指定字节数组
     * @return index 或者 -1 (未找到)
     */
    int find(byte[] b);

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
     */
    default byte[] readFind(byte b) {
        var index = find(b);
        if (index == -1) {
            return null;
        }
        return read(index);
    }

    /**
     * 读取 直到 找到匹配的字节 (指针会移动)
     *
     * @param b 指定字节
     * @return bytes
     */
    default byte[] readFind(byte[] b) {
        var index = find(b);
        if (index == -1) {
            return null;
        }
        return read(index);
    }

}

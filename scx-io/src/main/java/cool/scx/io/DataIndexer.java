package cool.scx.io;

public interface DataIndexer {

    /**
     * 如果 没找到返回 -1 否则返回索引位置
     * 此方法会循环调用 所以注意内部处理
     * 建议每次都重新创建
     *
     * @param bytes    bytes
     * @param position position
     * @param length   length
     * @return l
     */
    int indexOf(byte[] bytes, int position, int length);

}

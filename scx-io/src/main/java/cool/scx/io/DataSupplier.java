package cool.scx.io;

/**
 * 数据生产者
 */
public interface DataSupplier {

    /**
     * 获取数据 如果没有数据请返回 null
     *
     * @return 数据节点 或 null
     */
    DataNode get();

}

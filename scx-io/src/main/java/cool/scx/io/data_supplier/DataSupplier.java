package cool.scx.io.data_supplier;

import cool.scx.io.data_node.DataNode;

/// 数据生产者
/// @author scx567888
/// @version 0.0.1
public interface DataSupplier {

    /// 获取数据 如果没有数据请返回 null
    ///
    /// @return 数据节点 或 null
    DataNode get();

}

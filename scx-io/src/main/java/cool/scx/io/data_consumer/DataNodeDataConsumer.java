package cool.scx.io.data_consumer;

import cool.scx.io.data_node.DataNode;

/// 接受为单个 DataNode
///
/// @author scx567888
/// @version 0.0.1
public class DataNodeDataConsumer implements DataConsumer {

    private DataNode dataNode;

    @Override
    public boolean accept(byte[] bytes, int position, int length) {
        this.dataNode = new DataNode(bytes, position, position + length);
        //只接受一次
        return false;
    }

    public DataNode getDataNode() {
        return dataNode;
    }

}

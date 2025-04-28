package cool.scx.io.data_consumer;

import cool.scx.io.data_node.DataNode;

/// 接受为单个 DataNode 
public class DataNodeDataConsumer implements DataConsumer {

    private DataNode dataNode;

    @Override
    public boolean accept(byte[] bytes, int position, int length) {
        this.dataNode = new DataNode(bytes, position, length);
        //只接受一次
        return false;
    }

    public DataNode getDataNode() {
        return dataNode;
    }

}

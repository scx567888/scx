package cool.scx.byte_reader.consumer;

import cool.scx.byte_reader.ByteNode;

/// 接受为单个 DataNode
///
/// @author scx567888
/// @version 0.0.1
public class ByteNodeByteConsumer implements ByteConsumer {

    private ByteNode dataNode;

    @Override
    public boolean accept(byte[] bytes, int position, int length) {
        this.dataNode = new ByteNode(bytes, position, position + length);
        //只接受一次
        return false;
    }

    public ByteNode getDataNode() {
        return dataNode;
    }

}

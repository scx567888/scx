package cool.scx.io.data_consumer;

import cool.scx.io.IOHelper;
import cool.scx.io.data_node.DataNode;

/// ByteArrayDataConsumer
///
/// @author scx567888
/// @version 0.0.1
public class ByteArrayDataConsumer implements DataConsumer {

    private DataNode head;
    private DataNode tail;
    private int total;

    public ByteArrayDataConsumer() {
        this.head = null;
        this.tail = null;
        this.total = 0;
    }

    @Override
    public boolean accept(byte[] bytes, int position, int length) {
        total += length;
        var dataNode = new DataNode(bytes, position, position + length);
        if (head == null) {
            head = dataNode;
            tail = head;
        } else {
            tail.next = dataNode;
            tail = tail.next;
        }
        return true;
    }

    public byte[] getBytes() {
        var node = head;

        //从未调用 accept 会导致此情况
        if (node == null) {
            return new byte[0];
        }

        //只调用了一次 accept, 我们直接返回当前数据
        if (node.next == null) {
            return IOHelper.compressBytes(node.bytes, node.position, node.available());
        }

        //多个数据我们合并
        var bytes = new byte[total];
        int offset = 0;

        do {
            int length = node.available();
            System.arraycopy(node.bytes, node.position, bytes, offset, length);
            offset += length;
            node = node.next;
        } while (node != null);

        return bytes;
    }

}

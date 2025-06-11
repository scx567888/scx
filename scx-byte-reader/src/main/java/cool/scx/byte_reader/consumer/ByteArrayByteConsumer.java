package cool.scx.byte_reader.consumer;

import cool.scx.byte_reader.ByteNode;

/// ByteArrayDataConsumer
///
/// @author scx567888
/// @version 0.0.1
public class ByteArrayByteConsumer implements ByteConsumer {

    private ByteNode head;
    private ByteNode tail;
    private int total;

    public ByteArrayByteConsumer() {
        this.head = null;
        this.tail = null;
        this.total = 0;
    }

    public static byte[] compressBytes(byte[] bytes, int offset, int length) {
        if (offset == 0 && length == bytes.length) {
            return bytes;
        }
        var data = new byte[length];
        System.arraycopy(bytes, offset, data, 0, length);
        return data;
    }

    @Override
    public boolean accept(byte[] bytes, int position, int length) {
        total += length;
        var dataNode = new ByteNode(bytes, position, position + length);
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
            return compressBytes(node.bytes, node.position, node.available());
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

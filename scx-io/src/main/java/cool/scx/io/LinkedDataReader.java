package cool.scx.io;

import cool.scx.common.util.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.util.function.Supplier;

public class LinkedDataReader implements DataReader {

    private final Supplier<Node> dataSupplier;
    private Node head;
    private Node tail;

    public LinkedDataReader(Supplier<Node> dataSupplier) {
        this.dataSupplier = dataSupplier;
        this.head = new Node(EMPTY_BYTES);
        this.tail = this.head;
    }

    /**
     * @return 是否拉取成功
     */
    private boolean pullData() {
        var data = dataSupplier.get();
        if (data == null) {
            return false;
        }
        tail.next = data;
        tail = tail.next;
        return true;
    }

    private void ensureAvailable() {
        while (!head.hasAvailable()) {
            if (head.next == null) {
                if (!pullData()) {
                    throw new NoMoreDataException();
                }
            }
            head = head.next;
        }
    }

    private void walk(DataConsumer consumer, int maxLength, boolean movePointer) {
        ensureAvailable(); // 确保至少有一个字节可读

        var remaining = maxLength; // 剩余需要读取的字节数
        var n = head; // 用于循环的节点

        // 循环有 2 种情况会退出
        // 1, 已经读取到足够的数据
        // 2, 没有更多数据可读了
        while (remaining > 0) {
            // 计算当前节点可以读取的长度
            var length = Math.min(remaining, n.available());
            // 写入数据
            consumer.accept(n.bytes, n.position, length);
            // 计算剩余字节数
            remaining -= length;

            if (movePointer) {
                // 移动当前节点的指针位置
                n.position += length;
            }

            // 如果 remaining > 0 说明还需要继续读取
            if (remaining > 0) {
                // 如果 当前节点没有下一个节点 并且拉取失败 (没有更多数据) 则退出循环
                if (n.next == null && !this.pullData()) {
                    break;
                }
                n = n.next;
                if (movePointer) {
                    head = n;
                }
            }
        }
    }

    private int indexOf(DataIndexer indexer) {
        var index = 0; // 主串索引

        var n = head;

        while (n != null) {
            var length = n.available();
            var i = indexer.indexOf(n.bytes, n.position, length);
            if (i != -1) {
                return index + i;
            } else {
                index += length;
            }

            // 如果 currentNode 没有下一个节点并且尝试拉取数据失败，直接退出循环
            if (n.next == null) {
                var moreData = pullData();
                if (!moreData) {
                    break;
                }
            }
            n = n.next;
        }

        throw new NoMatchFoundException();
    }

    @Override
    public byte read() throws NoMoreDataException {
        ensureAvailable();
        return head.bytes[head.position++];
    }

    @Override
    public byte[] read(int maxLength) throws NoMoreDataException {
        var consumer = new BytesDataConsumer();
        walk(consumer, maxLength, true);
        return consumer.getBytes();
    }

    @Override
    public void read(DataConsumer dataConsumer, int maxLength) throws NoMoreDataException {
        walk(dataConsumer, maxLength, true);
    }

    @Override
    public byte peek() throws NoMoreDataException {
        ensureAvailable();
        return head.bytes[head.position];
    }

    @Override
    public byte[] peek(int maxLength) throws NoMoreDataException {
        var consumer = new BytesDataConsumer();
        walk(consumer, maxLength, false);
        return consumer.getBytes();
    }

    @Override
    public void peek(DataConsumer dataConsumer, int maxLength) throws NoMoreDataException {
        walk(dataConsumer, maxLength, false);
    }

    @Override
    public int indexOf(byte b) throws NoMatchFoundException {
        return indexOf((bytes, position, length) -> ArrayUtils.indexOf(bytes, position, length, b));
    }

    @Override
    public int indexOf(byte[] pattern) throws NoMatchFoundException {
        return indexOf(new KMPDataIndexer(pattern));
    }

    @Override
    public void skip(int length) {
        walk((_, _, _) -> {}, length, true);
    }

    public static class Node {
        private final byte[] bytes;
        private final int limit;
        private int position;
        private Node next;

        public Node(byte[] bytes) {
            this.bytes = bytes;
            this.position = 0;
            this.limit = bytes.length;
        }

        public Node(byte[] bytes, int position, int limit) {
            this.bytes = bytes;
            this.position = position;
            this.limit = limit;
        }

        int available() {
            return limit - position;
        }

        boolean hasAvailable() {
            return position < limit;
        }

        @Override
        public String toString() {
            return new String(bytes, position, limit - position);
        }

    }

}

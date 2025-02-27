package cool.scx.io.data_reader;

import cool.scx.io.data_consumer.ByteArrayDataConsumer;
import cool.scx.io.data_consumer.DataConsumer;
import cool.scx.io.data_indexer.ByteIndexer;
import cool.scx.io.data_indexer.DataIndexer;
import cool.scx.io.data_indexer.KMPDataIndexer;
import cool.scx.io.data_node.DataNode;
import cool.scx.io.data_supplier.DataSupplier;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;

import static cool.scx.io.data_consumer.SkipDataConsumer.SKIP_DATA_CONSUMER;
import static java.lang.Math.min;

/// LinkedDataReader
///
/// @author scx567888
/// @version 0.0.1
public class LinkedDataReader implements DataReader {

    public final DataSupplier dataSupplier;
    public DataNode head;
    public DataNode tail;

    public LinkedDataReader(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
        this.head = new DataNode(new byte[]{});
        this.tail = this.head;
    }

    public LinkedDataReader() {
        this(() -> null);
    }

    public void appendData(DataNode data) {
        tail.next = data;
        tail = tail.next;
    }

    public boolean pullData() {
        var data = dataSupplier.get();
        if (data == null) {
            return false;
        }
        appendData(data);
        return true;
    }

    public boolean ensureAvailable() {
        while (!head.hasAvailable()) {
            if (head.next == null) {
                var result = this.pullData();
                if (!result) {
                    return false;
                }
            }
            head = head.next;
        }
        return true;
    }

    public void ensureAvailableOrThrow() throws NoMoreDataException {
        var b = ensureAvailable();
        if (!b) {
            throw new NoMoreDataException();
        }
    }

    public void walk(DataConsumer consumer, long maxLength, boolean movePointer) {

        var remaining = maxLength; // 剩余需要读取的字节数
        var n = head; // 用于循环的节点

        // 循环有 2 种情况会退出
        // 1, 已经读取到足够的数据
        // 2, 没有更多数据可读了
        while (remaining > 0) {
            // 计算当前节点可以读取的长度 (这里因为是将 int 和 long 值进行最小值比较 所以返回值一定是 int 所以类型转换不会丢失精度) 
            var length = (int) min(remaining, n.available());
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
                if (n.next == null) {
                    // 如果 当前节点没有下一个节点 并且拉取失败 则退出循环
                    var result = this.pullData();
                    if (!result) {
                        break;
                    }
                }
                n = n.next;
                if (movePointer) {
                    head = n;
                }
            }
        }
    }

    public long indexOf(DataIndexer indexer, long max) throws NoMatchFoundException {

        var index = 0L; // 主串索引

        var n = head;

        while (true) {
            // 计算当前节点中可读取的最大长度，确保不超过 max (这里因为是将 int 和 long 值进行最小值比较 所以返回值一定是 int 所以类型转换不会丢失精度)
            var length = (int) min(n.available(), max - index);
            var i = indexer.indexOf(n.bytes, n.position, length);
            //此处因为支持回溯匹配 所以可能是负数 Integer.MIN_VALUE 表示真正未找到
            if (i != Integer.MIN_VALUE) {
                return index + i;
            } else {
                index += length;
            }

            // 检查是否已达到最大长度
            if (index >= max) {
                break;
            }

            // 如果 currentNode 没有下一个节点并且尝试拉取数据失败，直接退出循环
            if (n.next == null) {
                var result = this.pullData();
                if (!result) {
                    break;
                }
            }
            n = n.next;
        }

        throw new NoMatchFoundException();
    }

    @Override
    public byte read() throws NoMoreDataException {
        ensureAvailableOrThrow();
        var b = head.bytes[head.position];
        head.position = head.position + 1;
        return b;
    }

    @Override
    public byte[] read(int maxLength) throws NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow();
        }
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, true);
        return consumer.getBytes();
    }

    @Override
    public void read(DataConsumer dataConsumer, long maxLength) throws NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow();
        }
        walk(dataConsumer, maxLength, true);
    }

    @Override
    public byte peek() throws NoMoreDataException {
        ensureAvailableOrThrow();
        return head.bytes[head.position];
    }

    @Override
    public byte[] peek(int maxLength) throws NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow();
        }
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, false);
        return consumer.getBytes();
    }

    @Override
    public void peek(DataConsumer dataConsumer, long maxLength) throws NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow();
        }
        walk(dataConsumer, maxLength, false);
    }

    @Override
    public long indexOf(byte b, long maxLength) throws NoMatchFoundException, NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow();
        }
        return indexOf(new ByteIndexer(b), maxLength);
    }

    @Override
    public long indexOf(byte[] pattern, long maxLength) throws NoMatchFoundException, NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow();
        }
        return indexOf(new KMPDataIndexer(pattern), maxLength);
    }

    @Override
    public void skip(long length) throws NoMoreDataException {
        if (length > 0) {
            ensureAvailableOrThrow();
        }
        walk(SKIP_DATA_CONSUMER, length, true);
    }

}

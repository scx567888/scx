package cool.scx.io;

import cool.scx.io.DataPuller.PullResult;

import static cool.scx.io.DataPuller.PullResult.*;
import static cool.scx.io.SkipDataConsumer.SKIP_DATA_CONSUMER;

/**
 * LinkedDataReader
 *
 * @author scx567888
 * @version 0.0.1
 */
public class LinkedDataReader implements DataReader {

    protected final DataSupplier dataSupplier;
    protected final DataPuller dataPuller;
    protected DataNode head;
    protected DataNode tail;

    public LinkedDataReader(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
        this.dataPuller = this::pullData;
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

    public PullResult pullData() {
        var data = dataSupplier.get();
        if (data == null) {
            return FAIL;
        }
        appendData(data);
        return SUCCESS;
    }

    public boolean ensureAvailable(DataPuller dataPuller) {
        while (!head.hasAvailable()) {
            if (head.next == null) {
                var result = dataPuller.pull();
                if (FAIL == result) {
                    return false;
                } else if (BREAK == result) {
                    break;
                }
            }
            head = head.next;
        }
        return true;
    }

    public void ensureAvailableOrThrow(DataPuller dataPuller) throws NoMoreDataException {
        var b = ensureAvailable(dataPuller);
        if (!b) {
            throw new NoMoreDataException();
        }
    }

    public void walk(DataConsumer consumer, int maxLength, boolean movePointer, DataPuller dataPuller) {

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
                if (n.next == null) {
                    // 如果 当前节点没有下一个节点 并且拉取失败 则退出循环
                    var result = dataPuller.pull();
                    if (result != SUCCESS) {
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

    public int indexOf(DataIndexer indexer, int max, DataPuller dataPuller) throws NoMatchFoundException {

        var index = 0; // 主串索引

        var n = head;

        while (true) {
            // 计算当前节点中可读取的最大长度，确保不超过 max
            var length = Math.min(n.available(), max - index);
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
                var result = dataPuller.pull();
                if (result != SUCCESS) {
                    break;
                }
            }
            n = n.next;
        }

        throw new NoMatchFoundException();
    }

    public boolean ensureAvailable() throws NoMoreDataException {
        return ensureAvailable(dataPuller);
    }

    public void ensureAvailableOrThrow() throws NoMoreDataException {
        ensureAvailableOrThrow(dataPuller);
    }

    public void walk(DataConsumer consumer, int maxLength, boolean movePointer) {
        walk(consumer, maxLength, movePointer, dataPuller);
    }

    public int indexOf(DataIndexer indexer, int max) throws NoMatchFoundException {
        return indexOf(indexer, max, dataPuller);
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
        ensureAvailableOrThrow();
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, true);
        return consumer.getBytes();
    }

    @Override
    public void read(DataConsumer dataConsumer, int maxLength) throws NoMoreDataException {
        ensureAvailableOrThrow();
        walk(dataConsumer, maxLength, true);
    }

    @Override
    public byte peek() throws NoMoreDataException {
        ensureAvailableOrThrow();
        return head.bytes[head.position];
    }

    @Override
    public byte[] peek(int maxLength) throws NoMoreDataException {
        ensureAvailableOrThrow();
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, false);
        return consumer.getBytes();
    }

    @Override
    public void peek(DataConsumer dataConsumer, int maxLength) throws NoMoreDataException {
        ensureAvailableOrThrow();
        walk(dataConsumer, maxLength, false);
    }

    @Override
    public int indexOf(byte b, int max) throws NoMatchFoundException, NoMoreDataException {
        ensureAvailableOrThrow();
        return indexOf(new ByteIndexer(b), max);
    }

    @Override
    public int indexOf(byte[] pattern, int max) throws NoMatchFoundException, NoMoreDataException {
        ensureAvailableOrThrow();
        return indexOf(new KMPDataIndexer(pattern), max);
    }

    @Override
    public void skip(int length) throws NoMoreDataException {
        ensureAvailableOrThrow();
        walk(SKIP_DATA_CONSUMER, length, true);
    }

}

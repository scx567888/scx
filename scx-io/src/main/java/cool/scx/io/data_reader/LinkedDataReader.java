package cool.scx.io.data_reader;

import cool.scx.io.data_consumer.ByteArrayDataConsumer;
import cool.scx.io.data_consumer.DataConsumer;
import cool.scx.io.data_consumer.FillByteArrayDataConsumer;
import cool.scx.io.data_consumer.OutputStreamDataConsumer;
import cool.scx.io.data_indexer.ByteIndexer;
import cool.scx.io.data_indexer.DataIndexer;
import cool.scx.io.data_indexer.KMPDataIndexer;
import cool.scx.io.data_node.DataNode;
import cool.scx.io.data_supplier.DataSupplier;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;

import java.io.OutputStream;

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
    //标记
    public DataNode markNode;
    public int markNodePosition;

    public LinkedDataReader(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
        this.head = new DataNode(new byte[]{});
        this.tail = this.head;
        this.markNode = null;
        this.markNodePosition = 0;
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

    public long ensureAvailable(long maxPullCount) {
        var pullCount = 0L;
        while (!head.hasAvailable()) {
            if (head.next == null) {
                if (pullCount >= maxPullCount) {
                    break;
                }
                var result = this.pullData();
                if (!result) {
                    return -1;
                } else {
                    pullCount = pullCount + 1;
                }
            }
            head = head.next;
        }
        return pullCount;
    }

    public long ensureAvailableOrThrow(long maxPullCount) throws NoMoreDataException {
        var b = ensureAvailable(maxPullCount);
        if (b == -1) {
            throw new NoMoreDataException();
        } else {
            return b;
        }
    }

    public void walk(DataConsumer consumer, long maxLength, boolean movePointer, long maxPullCount) {

        var remaining = maxLength; // 剩余需要读取的字节数
        var n = head; // 用于循环的节点
        var pullCount = 0L; // 拉取次数计数器

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
                    if (pullCount >= maxPullCount) {
                        break;
                    }
                    // 如果 当前节点没有下一个节点 并且拉取失败 则退出循环
                    var result = this.pullData();
                    if (!result) {
                        break;
                    } else {
                        pullCount = pullCount + 1;
                    }
                }
                n = n.next;
                if (movePointer) {
                    head = n;
                }
            }
        }
    }

    public long indexOf(DataIndexer indexer, long maxLength, long maxPullCount) throws NoMatchFoundException {

        var index = 0L; // 主串索引

        var n = head;
        var pullCount = 0L; // 拉取次数计数器

        while (true) {
            // 计算当前节点中可读取的最大长度，确保不超过 max (这里因为是将 int 和 long 值进行最小值比较 所以返回值一定是 int 所以类型转换不会丢失精度)
            var length = (int) min(n.available(), maxLength - index);
            var i = indexer.indexOf(n.bytes, n.position, length);
            //此处因为支持回溯匹配 所以可能是负数 Integer.MIN_VALUE 表示真正未找到
            if (i != Integer.MIN_VALUE) {
                return index + i;
            } else {
                index += length;
            }

            // 检查是否已达到最大长度
            if (index >= maxLength) {
                break;
            }

            // 如果 currentNode 没有下一个节点并且尝试拉取数据失败，直接退出循环
            if (n.next == null) {
                if (pullCount >= maxPullCount) {
                    break;
                }
                var result = this.pullData();
                if (!result) {
                    break;
                } else {
                    pullCount = pullCount + 1;
                }
            }
            n = n.next;
        }

        throw new NoMatchFoundException();
    }

    @Override
    public byte read() throws NoMoreDataException {
        ensureAvailableOrThrow(Long.MAX_VALUE);
        var b = head.bytes[head.position];
        head.position = head.position + 1;
        return b;
    }

    @Override
    public byte[] read(int maxLength) throws NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow(Long.MAX_VALUE);
        }
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, true, Long.MAX_VALUE);
        return consumer.getBytes();
    }

    @Override
    public byte[] read(int maxLength, long maxPullCount) throws NoMoreDataException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, true, maxPullCount);
        return consumer.getBytes();
    }

    @Override
    public void read(DataConsumer dataConsumer, long maxLength) throws NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow(Long.MAX_VALUE);
        }
        walk(dataConsumer, maxLength, true, Long.MAX_VALUE);
    }

    @Override
    public void read(DataConsumer dataConsumer, long maxLength, long maxPullCount) throws NoMoreDataException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        walk(dataConsumer, maxLength, true, maxPullCount);
    }

    @Override
    public byte peek() throws NoMoreDataException {
        ensureAvailableOrThrow(Long.MAX_VALUE);
        return head.bytes[head.position];
    }

    @Override
    public byte[] peek(int maxLength) throws NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow(Long.MAX_VALUE);
        }
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, false, Long.MAX_VALUE);
        return consumer.getBytes();
    }

    @Override
    public byte[] peek(int maxLength, long maxPullCount) throws NoMoreDataException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, false, maxPullCount);
        return consumer.getBytes();
    }

    @Override
    public void peek(DataConsumer dataConsumer, long maxLength) throws NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow(Long.MAX_VALUE);
        }
        walk(dataConsumer, maxLength, false, Long.MAX_VALUE);
    }

    @Override
    public void peek(DataConsumer dataConsumer, long maxLength, long maxPullCount) throws NoMoreDataException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        walk(dataConsumer, maxLength, false, maxPullCount);
    }

    @Override
    public long indexOf(byte b, long maxLength) throws NoMatchFoundException, NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow(Long.MAX_VALUE);
        }
        return indexOf(new ByteIndexer(b), maxLength, Long.MAX_VALUE);
    }

    @Override
    public long indexOf(byte b, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        return indexOf(new ByteIndexer(b), maxLength, maxPullCount);
    }

    @Override
    public long indexOf(byte[] pattern, long maxLength) throws NoMatchFoundException, NoMoreDataException {
        if (maxLength > 0) {
            ensureAvailableOrThrow(Long.MAX_VALUE);
        }
        return indexOf(new KMPDataIndexer(pattern), maxLength, Long.MAX_VALUE);
    }

    @Override
    public long indexOf(byte[] pattern, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        return indexOf(new KMPDataIndexer(pattern), maxLength, maxPullCount);
    }

    @Override
    public void skip(long length) throws NoMoreDataException {
        if (length > 0) {
            ensureAvailableOrThrow(Long.MAX_VALUE);
        }
        walk(SKIP_DATA_CONSUMER, length, true, Long.MAX_VALUE);
    }

    @Override
    public void skip(long length, long maxPullCount) throws NoMoreDataException {
        if (length > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        walk(SKIP_DATA_CONSUMER, length, true, maxPullCount);
    }

    @Override
    public void mark() {
        markNode = head;
        markNodePosition = head.position;
    }

    @Override
    public void reset() {
        if (markNode == null) {
            return;
        }
        //重置当前 mark
        head = markNode;
        head.position = markNodePosition;
        //后续节点全部重置
        var n = head.next;
        while (n != null) {
            n.reset();
            n = n.next;
        }
    }

    @Override
    public int inputStreamRead() {
        var pullCount = ensureAvailable(1);
        if (pullCount == -1) {
            return -1;
        }
        var b = head.bytes[head.position];
        head.position = head.position + 1;
        return b & 0xFF;
    }

    @Override
    public int inputStreamRead(byte[] b, int off, int len) {
        var maxPullCount = 1L;
        if (len > 0) {
            var pullCount = ensureAvailable(maxPullCount);
            if (pullCount == -1) {
                return -1;
            } else {
                maxPullCount = maxPullCount - pullCount;
            }
        }
        var consumer = new FillByteArrayDataConsumer(b, off, len);
        walk(consumer, len, true, maxPullCount);
        return consumer.getFilledLength();
    }

    @Override
    public long inputStreamTransferTo(OutputStream out, long maxLength) {
        if (maxLength > 0) {
            var pullCount = ensureAvailable(Long.MAX_VALUE);
            if (pullCount == -1) {
                return -1;
            }
        }
        var consumer = new OutputStreamDataConsumer(out);
        walk(consumer, maxLength, true, Long.MAX_VALUE);
        return consumer.byteCount();
    }

}

package cool.scx.io.data_reader;

import cool.scx.io.data_consumer.DataConsumer;
import cool.scx.io.data_consumer.FillByteArrayDataConsumer;
import cool.scx.io.data_consumer.OutputStreamDataConsumer;
import cool.scx.io.data_indexer.DataIndexer;
import cool.scx.io.data_node.DataNode;
import cool.scx.io.data_supplier.DataSupplier;
import cool.scx.io.exception.DataSupplierException;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;

import java.io.OutputStream;

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

    public boolean pullData() throws DataSupplierException {
        var data = dataSupplier.get();
        if (data == null) {
            return false;
        }
        appendData(data);
        return true;
    }

    public long ensureAvailable(long maxPullCount) throws DataSupplierException {
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

    public long ensureAvailableOrThrow(long maxPullCount) throws NoMoreDataException, DataSupplierException {
        var b = ensureAvailable(maxPullCount);
        if (b == -1) {
            throw new NoMoreDataException();
        } else {
            return b;
        }
    }

    public void walk(DataConsumer consumer, long maxLength, boolean movePointer, long maxPullCount) throws DataSupplierException {

        var remaining = maxLength; // 剩余需要读取的字节数
        var n = head; // 用于循环的节点
        var pullCount = 0L; // 拉取次数计数器
        var needMore = true;// 是否需要更多数据 我们默认是 true 表示 至少执行调用一次消费者

        // 循环中有 4 种情况
        // 1, 已经读取到足够的数据 我们无需循环
        // 2, 消费者返回 false 我们会至少循环一次
        // 3, 达到最大拉取次数 我们会至少循环一次
        // 4, 没有更多数据了 我们会至少循环一次

        // 初始只判断是否 已经读取到足够的数据
        while (remaining > 0) {

            // 计算当前节点可以读取的长度 (这里因为是将 int 和 long 值进行最小值比较 所以返回值一定是 int 所以类型转换不会丢失精度) 
            var length = (int) min(remaining, n.available());
            // 调用消费者 写入数据
            needMore = consumer.accept(n.bytes, n.position, length);
            // 计算剩余字节数
            remaining -= length;

            if (movePointer) {
                // 移动当前节点的指针位置
                n.position += length;
            }

            // 数据已经读取够 或者 无需继续读取了 我们直接跳出循环
            if (remaining <= 0 || !needMore) {
                break;
            }

            // 当走到这里时 说明 remaining 一定大于 0,
            // 而从 remaining 和 length 的计算方式得出 此时 n 一定已经被彻底消耗掉了
            // 所以我们可以放心的直接更新到下一节点 即可

            if (n.next == null) {
                //已经达到最大拉取次数 直接退出
                if (pullCount >= maxPullCount) {
                    break;
                }
                // 如果 当前节点没有下一个节点 并且拉取失败 则退出循环
                var result = this.pullData();
                if (!result) {
                    break;
                }
                pullCount = pullCount + 1;
            }
            n = n.next;

            //更新 头节点
            if (movePointer) {
                head = n;
            }

        }

    }

    public long findIndex(DataIndexer indexer, long maxLength, long maxPullCount) throws NoMatchFoundException, DataSupplierException {

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
                }
                pullCount = pullCount + 1;
            }
            n = n.next;

        }

        throw new NoMatchFoundException();
    }

    @Override
    public byte read() throws NoMoreDataException, DataSupplierException {
        ensureAvailableOrThrow(Long.MAX_VALUE);
        var b = head.bytes[head.position];
        head.position = head.position + 1;
        return b;
    }

    @Override
    public void read(DataConsumer dataConsumer, long maxLength, long maxPullCount) throws NoMoreDataException, DataSupplierException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        walk(dataConsumer, maxLength, true, maxPullCount);
    }

    @Override
    public byte peek() throws NoMoreDataException, DataSupplierException {
        ensureAvailableOrThrow(Long.MAX_VALUE);
        return head.bytes[head.position];
    }

    @Override
    public void peek(DataConsumer dataConsumer, long maxLength, long maxPullCount) throws NoMoreDataException, DataSupplierException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        walk(dataConsumer, maxLength, false, maxPullCount);
    }

    @Override
    public long indexOf(DataIndexer indexer, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException, DataSupplierException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        return findIndex(indexer, maxLength, maxPullCount);
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
    public int inputStreamRead() throws DataSupplierException {
        var pullCount = ensureAvailable(1);
        if (pullCount == -1) {
            return -1;
        }
        var b = head.bytes[head.position];
        head.position = head.position + 1;
        return b & 0xFF;
    }

    @Override
    public int inputStreamRead(byte[] b, int off, int len) throws DataSupplierException {
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
    public long inputStreamTransferTo(OutputStream out, long maxLength) throws DataSupplierException {
        if (maxLength > 0) {
            var pullCount = ensureAvailable(Long.MAX_VALUE);
            if (pullCount == -1) {
                return 0;
            }
        }
        var consumer = new OutputStreamDataConsumer(out);
        walk(consumer, maxLength, true, Long.MAX_VALUE);
        return consumer.byteCount();
    }

}

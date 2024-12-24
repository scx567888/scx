package cool.scx.io;

import cool.scx.common.util.ArrayUtils;
import cool.scx.io.DataPuller.PullResult;

import java.util.function.Supplier;

import static cool.scx.io.DataPuller.PullResult.*;

/**
 * LinkedDataReader
 *
 * @author scx567888
 * @version 0.0.1
 */
public class LinkedDataReader implements DataReader {

    private final Supplier<DataNode> dataSupplier;
    private DataNode head;
    private DataNode tail;

    public LinkedDataReader(Supplier<DataNode> dataSupplier) {
        this.dataSupplier = dataSupplier;
        this.head = new DataNode(EMPTY_BYTES);
        this.tail = this.head;
    }

    public LinkedDataReader() {
        this(() -> null);
    }

    public void ensureAvailable(DataPuller dataPuller) throws NoMoreDataException {
        while (!head.hasAvailable()) {
            if (head.next == null) {
                var result = dataPuller.pull();
                if (FAIL == result) {
                    throw new NoMoreDataException();
                } else if (BREAK == result) {
                    break;
                }
            }
            head = head.next;
        }
    }

    public void walk(DataConsumer consumer, int maxLength, boolean movePointer, DataPuller dataPuller) throws NoMoreDataException {
        ensureAvailable(dataPuller); // 确保至少有一个字节可读

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

    public int indexOf(DataIndexer indexer, int max, DataPuller dataPuller) throws NoMoreDataException, NoMatchFoundException {
        ensureAvailable(dataPuller); // 确保至少有一个字节可读

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

    /**
     * 添加数据
     *
     * @param data data
     */
    public void appendData(DataNode data) {
        tail.next = data;
        tail = tail.next;
    }

    /**
     * 拉取数据
     *
     * @return 是否拉取成功
     */
    public PullResult pullData() {
        var data = dataSupplier.get();
        if (data == null) {
            return FAIL;
        }
        appendData(data);
        return SUCCESS;
    }

    public byte read(DataPuller dataPuller) throws NoMoreDataException {
        ensureAvailable(dataPuller);
        var b = head.bytes[head.position];
        head.position = head.position + 1;
        return b;
    }

    public byte[] read(int maxLength, DataPuller dataPuller) throws NoMoreDataException {
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, true, dataPuller);
        return consumer.getBytes();
    }

    public void read(DataConsumer dataConsumer, int maxLength, DataPuller dataPuller) throws NoMoreDataException {
        walk(dataConsumer, maxLength, true, dataPuller);
    }

    public byte peek(DataPuller dataPuller) throws NoMoreDataException {
        ensureAvailable(dataPuller);
        return head.bytes[head.position];
    }

    public byte[] peek(int maxLength, DataPuller dataPuller) throws NoMoreDataException {
        var consumer = new ByteArrayDataConsumer();
        walk(consumer, maxLength, false, dataPuller);
        return consumer.getBytes();
    }

    public void peek(DataConsumer dataConsumer, int maxLength, DataPuller dataPuller) throws NoMoreDataException {
        walk(dataConsumer, maxLength, false, dataPuller);
    }

    public int indexOf(byte b, int max, DataPuller dataPuller) throws NoMatchFoundException, NoMoreDataException {
        return indexOf((bytes, position, length) -> {
            int i = ArrayUtils.indexOf(bytes, position, length, b);
            return i == -1 ? Integer.MIN_VALUE : i;
        }, max, dataPuller);
    }

    public int indexOf(byte[] pattern, int max, DataPuller dataPuller) throws NoMatchFoundException, NoMoreDataException {
        return indexOf(new KMPDataIndexer(pattern), max, dataPuller);
    }

    public void skip(int length, DataPuller dataPuller) throws NoMoreDataException {
        walk((_, _, _) -> {}, length, true, dataPuller);
    }

}

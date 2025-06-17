package cool.scx.bytes;

import cool.scx.bytes.consumer.ByteArrayByteConsumer;
import cool.scx.bytes.consumer.ByteConsumer;
import cool.scx.bytes.consumer.FillByteArrayByteConsumer;
import cool.scx.bytes.consumer.OutputStreamByteConsumer;
import cool.scx.bytes.exception.ByteSupplierException;
import cool.scx.bytes.exception.NoMatchFoundException;
import cool.scx.bytes.exception.NoMoreDataException;
import cool.scx.bytes.indexer.ByteIndexer;
import cool.scx.bytes.supplier.ByteSupplier;

import java.io.IOException;
import java.io.OutputStream;

import static java.lang.Math.min;

/// ByteReader
///
/// @author scx567888
/// @version 0.0.1
public class ByteReader implements IByteReader {

    public final ByteSupplier byteSupplier;
    public ByteNode head;
    public ByteNode tail;
    //标记
    public ByteNode markNode;
    public int markNodePosition;

    public ByteReader(ByteSupplier byteSupplier) {
        this.byteSupplier = byteSupplier;
        this.head = new ByteNode(new ByteChunk(new byte[]{}));
        this.tail = this.head;
        this.markNode = null;
        this.markNodePosition = 0;
    }

    public ByteReader() {
        this(() -> null);
    }

    public void appendByteChunk(ByteChunk byteChunk) {
        tail.next = new ByteNode(byteChunk);
        tail = tail.next;
    }

    public boolean pullByteChunk() throws ByteSupplierException {
        var byteChunk = byteSupplier.get();
        if (byteChunk == null) {
            return false;
        }
        appendByteChunk(byteChunk);
        return true;
    }

    public long ensureAvailable(long maxPullCount) throws ByteSupplierException {
        var pullCount = 0L;
        while (!head.hasAvailable()) {
            if (head.next == null) {
                if (pullCount >= maxPullCount) {
                    break;
                }
                var result = this.pullByteChunk();
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

    public long ensureAvailableOrThrow(long maxPullCount) throws NoMoreDataException, ByteSupplierException {
        var b = ensureAvailable(maxPullCount);
        if (b == -1) {
            throw new NoMoreDataException();
        } else {
            return b;
        }
    }

    public void walk(ByteConsumer consumer, long maxLength, boolean movePointer, long maxPullCount) throws ByteSupplierException {

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
            needMore = consumer.accept(n.chunk.subChunk(n.position, n.position + length));
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
                var result = this.pullByteChunk();
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

    public long findIndex(ByteIndexer indexer, long maxLength, long maxPullCount) throws NoMatchFoundException, ByteSupplierException {

        var index = 0L; // 主串索引

        var n = head;
        var pullCount = 0L; // 拉取次数计数器

        while (true) {
            // 计算当前节点中可读取的最大长度, 确保不超过 max (这里因为是将 int 和 long 值进行最小值比较 所以返回值一定是 int 所以类型转换不会丢失精度)
            var length = (int) min(n.available(), maxLength - index);
            var i = indexer.indexOf(n.chunk.subChunk(n.position, n.position + length));
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

            // 如果 currentNode 没有下一个节点并且尝试拉取数据失败, 直接退出循环
            if (n.next == null) {
                if (pullCount >= maxPullCount) {
                    break;
                }
                var result = this.pullByteChunk();
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
    public byte read() throws NoMoreDataException, ByteSupplierException {
        ensureAvailableOrThrow(Long.MAX_VALUE);
        var b = head.chunk.getByte(head.position);
        head.position = head.position + 1;
        return b;
    }

    @Override
    public void read(ByteConsumer byteConsumer, long maxLength, long maxPullCount) throws NoMoreDataException, ByteSupplierException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        walk(byteConsumer, maxLength, true, maxPullCount);
    }

    @Override
    public byte peek() throws NoMoreDataException, ByteSupplierException {
        ensureAvailableOrThrow(Long.MAX_VALUE);
        return head.chunk.getByte(head.position);
    }

    @Override
    public void peek(ByteConsumer byteConsumer, long maxLength, long maxPullCount) throws NoMoreDataException, ByteSupplierException {
        if (maxLength > 0) {
            var pullCount = ensureAvailableOrThrow(maxPullCount);
            maxPullCount = maxPullCount - pullCount;
        }
        walk(byteConsumer, maxLength, false, maxPullCount);
    }

    @Override
    public long indexOf(ByteIndexer indexer, long maxLength, long maxPullCount) throws NoMatchFoundException, NoMoreDataException, ByteSupplierException {
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
    public int inputStreamRead() throws IOException {
        try {
            var pullCount = ensureAvailable(1);
            if (pullCount == -1) {
                return -1;
            }
            var b = head.chunk.getByte(head.position);
            head.position = head.position + 1;
            return b & 0xFF;
        } catch (ByteSupplierException e) {
            if (e.getCause() instanceof IOException i) {
                throw i;
            }
            throw e;
        }
    }

    @Override
    public int inputStreamRead(byte[] b, int off, int len) throws IOException {
        try {
            var maxPullCount = 1L;
            if (len > 0) {
                var pullCount = ensureAvailable(maxPullCount);
                if (pullCount == -1) {
                    return -1;
                } else {
                    maxPullCount = maxPullCount - pullCount;
                }
            }
            var consumer = new FillByteArrayByteConsumer(b, off, len);
            walk(consumer, len, true, maxPullCount);
            return consumer.getFilledLength();
        } catch (ByteSupplierException e) {
            if (e.getCause() instanceof IOException i) {
                throw i;
            }
            throw e;
        }
    }

    @Override
    public long inputStreamTransferTo(OutputStream out, long maxLength) throws IOException {
        try {
            if (maxLength > 0) {
                var pullCount = ensureAvailable(Long.MAX_VALUE);
                if (pullCount == -1) {
                    return 0;
                }
            }
            var consumer = new OutputStreamByteConsumer(out);
            walk(consumer, maxLength, true, Long.MAX_VALUE);
            return consumer.byteCount();
        } catch (ByteSupplierException e) {
            if (e.getCause() instanceof IOException i) {
                throw i;
            }
            throw e;
        }
    }

    @Override
    public byte[] inputStreamReadNBytes(long len) throws IOException {
        try {
            if (len > 0) {
                var pullCount = ensureAvailable(Long.MAX_VALUE);
                if (pullCount == -1) {
                    return new byte[0];
                }
            }
            var consumer = new ByteArrayByteConsumer();
            walk(consumer, len, true, Long.MAX_VALUE);
            return consumer.getBytes();
        } catch (ByteSupplierException e) {
            if (e.getCause() instanceof IOException i) {
                throw i;
            }
            throw e;
        }
    }

}

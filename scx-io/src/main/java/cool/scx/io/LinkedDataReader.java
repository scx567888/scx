package cool.scx.io;

import cool.scx.common.util.ArrayUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static cool.scx.io.KMPHelper.computeLPSArray;

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

    /**
     * @param pullOnce 是否只尝试拉取一次
     * @return 是否执行了拉取操作
     */
    private boolean ensureAvailable(boolean pullOnce) {
        while (!head.hasAvailable()) {
            if (head.next == null) {
                if (!pullData()) {
                    throw new NoMoreDataException();
                }
                if (pullOnce) {
                    return true;
                }
            }
            head = head.next;
        }
        return false;
    }

    private int read(ReadConsumer consumer, int maxLength, boolean movePointer, DataPuller puller) {
        var remaining = maxLength; // 剩余需要读取的字节数
        var n = head; // 用于循环的节点

        // 循环有 2 种情况会退出
        // 1, 已经读取到足够的数据
        // 2, 没有更多数据可读了
        while (remaining > 0) {
            // 计算当前节点可以读取的长度
            var toAdd = Math.min(remaining, n.available());
            // 写入数据
            consumer.accept(n.bytes, n.position, remaining, toAdd);
            // 计算剩余字节数
            remaining -= toAdd;

            if (movePointer) {
                // 移动当前节点的指针位置
                n.position += toAdd;
            }

            // 如果 remaining > 0 说明还需要继续读取
            // 但是 我们在上边的代码是一定会将 当前节点全部读完的 所以这里我们需要向后移动节点
            if (remaining > 0) {
                // 如果 当前节点没有下一个节点 则尝试拉取下一个节点
                if (n.next == null) {
                    //如果 返回 false 则表示拉取失败或不需要拉取 退出循环
                    if (!puller.pull()) {
                        break;
                    }
                }
                //更新 n 节点 的同时更新 head 节点 然后进行下一次循环
                if (movePointer) {
                    head = n = n.next;
                } else {
                    n = n.next;
                }
            }
        }
        return remaining;
    }

    private int read(ReadConsumer consumer, int maxLength, boolean movePointer) {
        ensureAvailable(false); // 确保至少有一个字节可读 之后持续拉取
        return read(consumer, maxLength, movePointer, this::pullData);
    }

    private int fastRead(ReadConsumer consumer, int maxLength, boolean movePointer) {
        var pulled = new AtomicBoolean(ensureAvailable(true)); // 只在这里尝试拉取一次 如果没拉取 后续可能会拉取
        return read(consumer, maxLength, movePointer, () -> {
            if (pulled.get()) {
                return false;
            } else {
                // 2, 如果没有拉取过 尝试拉取
                var moreData = pullData();
                pulled.set(true);
                // 如果拉取数据失败，则跳出循环
                return moreData;
            }
        });
    }

    private int tryRead(ReadConsumer consumer, int maxLength, boolean movePointer) {
        ensureAvailable(true); // 只在这里尝试拉取一次 之后一次都不拉取
        return read(consumer, maxLength, movePointer, () -> false);
    }

    @Override
    public byte read() throws NoMoreDataException {
        ensureAvailable(false);
        return head.bytes[head.position++];
    }

    @Override
    public byte[] read(int maxLength) throws NoMoreDataException {
        var result = new byte[maxLength];
        var r = read((bytes, position, remaining, toAdd) -> System.arraycopy(bytes, position, result, maxLength - remaining, toAdd), maxLength, true);
        return r == 0 ? result : Arrays.copyOf(result, maxLength - r);
    }

    @Override
    public void read(OutputStream outputStream, int maxLength) throws NoMoreDataException {
        read((bytes, position, _, toAdd) -> {
            // 写入到 result 中
            try {
                outputStream.write(bytes, position, toAdd);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, maxLength, true);
    }

    @Override
    public byte peek() throws NoMoreDataException {
        ensureAvailable(false);
        return head.bytes[head.position];
    }

    @Override
    public byte[] peek(int maxLength) throws NoMoreDataException {
        var result = new byte[maxLength];
        var r = read((bytes, position, remaining, toAdd) -> System.arraycopy(bytes, position, result, maxLength - remaining, toAdd), maxLength, false);
        return r == 0 ? result : Arrays.copyOf(result, maxLength - r);
    }

    @Override
    public void peek(OutputStream outputStream, int maxLength) throws NoMoreDataException {
        read((bytes, position, _, toAdd) -> {
            // 写入到 result 中
            try {
                outputStream.write(bytes, position, toAdd);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, maxLength, false);
    }


    @Override
    public int indexOf(byte b) throws NoMatchFoundException {
        var index = 0;

        var n = head;

        while (n != null) {
            //普通查找
            int pos = ArrayUtils.indexOf(n.bytes, n.position, n.bytes.length - n.position, b);
            if (pos != -1) {
                return index + (pos - n.position);
            }
            index += n.bytes.length - n.position;

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
    public int indexOf(byte[] pattern) throws NoMatchFoundException {
        var index = 0; // 主串索引

        var n = head;

        // 创建部分匹配表
        int[] lps = computeLPSArray(pattern);

        int patternIndex = 0; // 模式串索引

        while (n != null) {
            //KMP 查找
            for (int i = n.position; i < n.bytes.length; i++) {
                while (patternIndex > 0 && n.bytes[i] != pattern[patternIndex]) {
                    patternIndex = lps[patternIndex - 1];
                }

                if (n.bytes[i] == pattern[patternIndex]) {
                    patternIndex++;
                }

                if (patternIndex == pattern.length) {
                    // i - n.position 的意义在于我们不需要包含当前 节点的偏移量 所以减去
                    return index + (i - n.position - patternIndex + 1);
                }
            }
            index += n.bytes.length - n.position;

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
    public void skip(int length) {
        read((_, _, _, _) -> {}, length, true);
    }

    public byte[] fastRead(int maxLength) throws NoMoreDataException {
        var result = new byte[maxLength];
        var r = fastRead((bytes, position, remaining, toAdd) -> System.arraycopy(bytes, position, result, maxLength - remaining, toAdd), maxLength, true);
        return r == 0 ? result : Arrays.copyOf(result, maxLength - r);
    }

    public byte[] fastPeek(int maxLength) throws NoMoreDataException {
        var result = new byte[maxLength];
        var r = fastRead((bytes, position, remaining, toAdd) -> System.arraycopy(bytes, position, result, maxLength - remaining, toAdd), maxLength, false);
        return r == 0 ? result : Arrays.copyOf(result, maxLength - r);
    }

    public byte[] tryRead(int maxLength) throws NoMoreDataException {
        var result = new byte[maxLength];
        var r = tryRead((bytes, position, remaining, toAdd) -> System.arraycopy(bytes, position, result, maxLength - remaining, toAdd), maxLength, true);
        return r == 0 ? result : Arrays.copyOf(result, maxLength - r);
    }

    public byte[] tryPeek(int maxLength) throws NoMoreDataException {
        var result = new byte[maxLength];
        var r = tryRead((bytes, position, remaining, toAdd) -> System.arraycopy(bytes, position, result, maxLength - remaining, toAdd), maxLength, false);
        return r == 0 ? result : Arrays.copyOf(result, maxLength - r);
    }

    private interface ReadConsumer {
        void accept(byte[] bytes, int position, int remaining, int toAdd);
    }

    private interface DataPuller {
        boolean pull();
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

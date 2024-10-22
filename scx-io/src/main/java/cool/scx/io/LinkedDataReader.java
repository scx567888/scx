package cool.scx.io;

import cool.scx.common.util.ArrayUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.function.Supplier;

import static cool.scx.io.ScxIOHelper.computeLPSArray;

public class LinkedDataReader implements DataReader {

    private final Supplier<byte[]> bytesSupplier;
    private Node head;
    private Node tail;

    public LinkedDataReader(Supplier<byte[]> bytesSupplier) {
        this.bytesSupplier = bytesSupplier;
        this.head = new Node(EMPTY_BYTES);
        this.tail = this.head;
    }

    private boolean pullData() {
        var bytes = bytesSupplier.get();
        if (bytes == null) {
            return false;
        }
        tail.next = new Node(bytes);
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

    /**
     * 只尝试 pullData 一次
     * 对比 :
     * ensureAvailable 如果 pullData 拉取的是空字节数组 则会一直尝试拉取 直到 有一个可读的字节
     * fastEnsureAvailable 只要 pullData 拉取的不是 null 则拉取一次即可
     *
     * @return 是否执行了 pullData
     */
    private boolean fastEnsureAvailable() {
        while (!head.hasAvailable()) {
            if (head.next == null) {
                if (!pullData()) {
                    throw new NoMoreDataException();
                }
                //拉取一次即终止
                return true;
            }
            head = head.next;
        }
        return false;
    }

    private int read(ReadConsumer consumer, int maxLength, boolean movePointer) {
        ensureAvailable(); // 确保至少有一个字节可读

        var remaining = maxLength; // 剩余需要读取的字节数
        var n = head; // 用于循环的节点

        // 循环有两种情况会退出
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
                    var moreData = pullData();
                    //如果拉取失败 直接跳出循环
                    if (!moreData) {
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

    /**
     * 功能和 read 基本相同 但是当数据不足需要 pullData 的时候 只会拉取一次 而不是一直尝试拉取
     * 一般用于 网络 可以保证快速访问数据
     *
     * @param consumer    a
     * @param maxLength   a
     * @param movePointer a
     * @return a
     */
    private int fastRead(ReadConsumer consumer, int maxLength, boolean movePointer) {
        var pulled = fastEnsureAvailable(); // 不确保一定有数据可读 (只拉取一次)

        var remaining = maxLength; // 剩余需要读取的字节数
        var n = head; // 用于循环的节点

        // 循环有三种情况会退出
        // 1, 已经读取到足够的数据
        // 2, 没有更多数据可读了
        // 3, 已经拉取了
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
                    // 检查是否已经拉取过数据, 
                    // 如果拉取过 直接退出循环 指针移动问题我们不需要考虑 因为 fastEnsureAvailable 会处理
                    if (pulled) {
                        break;
                    } else {
                        // 2, 如果没有拉取过 尝试拉取
                        var moreData = pullData();
                        pulled = true;
                        // 如果拉取数据失败，则跳出循环
                        if (!moreData) {
                            break;
                        }
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

    @Override
    public byte read() throws NoMoreDataException {
        ensureAvailable();
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
        ensureAvailable();
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

    private interface ReadConsumer {
        void accept(byte[] bytes, int position, int remaining, int toAdd);
    }

    private static class Node {
        private final byte[] bytes;
        private int position;
        private Node next;

        Node(byte[] bytes) {
            this.bytes = bytes;
        }

        int available() {
            return bytes.length - position;
        }

        boolean hasAvailable() {
            return position < bytes.length;
        }

    }

}

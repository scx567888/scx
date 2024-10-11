package cool.scx.io;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Supplier;


public class LinkedDataReader implements DataReader {

    private final Supplier<byte[]> bytesSupplier;
    private Node head;
    private Node tail;

    public LinkedDataReader(Supplier<byte[]> bytesSupplier) {
        this.bytesSupplier = bytesSupplier;
        this.head = new Node(EMPTY_BYTES);
        this.tail = this.head;
    }

    public int available() {
        int a = 0;
        for (Node n = head; n != null; n = n.next) {
            a += n.available();
        }
        return a;
    }

    public void pullData() {
        byte[] bytes = bytesSupplier.get();
        if (bytes == null) {
//            throw new NoSuchElementException();
            return;
        }
        Node n = new Node(bytes);
        tail.next = n;
        tail = n;
    }

    public void ensureAvailable() {
        while (!head.hasAvailable()) {
            if (head.next == null) {
                pullData();
            }
            head = head.next;
        }
    }


    @Override
    public byte read() {
        ensureAvailable();
        return head.bytes[head.position++];
    }

    @Override
    public byte[] read(int len) {
        ensureAvailable(); // we have at least 1 byte
        byte[] b = new byte[len];

        if (len <= head.available()) { // fast case
            System.arraycopy(head.bytes, head.position, b, 0, len);
            head.position += len;
            return b;
        } else {
            int remaining = len;
            for (Node n = head; remaining > 0; n = n.next) {
                ensureAvailable();
                int toAdd = Math.min(remaining, n.available());
                System.arraycopy(n.bytes, n.position, b, len - remaining, toAdd);
                remaining -= toAdd;
                n.position += toAdd;
                if (remaining > 0 && n.next == null) {
                    pullData();
                }
            }
            return b;
        }
    }

    //todo 另一种 read 的实现 性能待测试
    public byte[] read1(int len) {
        var data = get(len);
        skip(len);
        return data;
    }

    @Override
    public byte get() {
        ensureAvailable();
        return head.bytes[head.position];
    }

    @Override
    public byte[] get(int len) {
        ensureAvailable(); // we have at least 1 byte
        byte[] b = new byte[len];

        if (len <= head.available()) { // fast case
            System.arraycopy(head.bytes, head.position, b, 0, len);
            return b;
        } else {
            int remaining = len;
            for (Node n = head; remaining > 0; n = n.next) {
                int toAdd = Math.min(remaining, n.available());
                System.arraycopy(n.bytes, n.position, b, len - remaining, toAdd);
                remaining -= toAdd;
                if (remaining > 0 && n.next == null) {
                    pullData();
                }
            }
            return b;
        }
    }

    @Override
    public int find(byte b) {
        ensureAvailable();
        int idx = 0;
        Node n = head;
        while (n != null) {
            for (int i = n.position; i < n.bytes.length; i++, idx++) {
                if (n.bytes[i] == b) {
                    return idx;
                }
            }
            n = n.next();
        }
        return -1;
    }

    @Override
    public int find(byte[] b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void skip(int lenToSkip) {
        while (lenToSkip > 0) {
            ensureAvailable();
            lenToSkip = head.skip(lenToSkip);
        }
    }

    private class Node {
        private final byte[] bytes;
        private int position;
        private Node next;

        Node(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public String toString() {
            return position + " of " + Arrays.toString(bytes);
        }

        int available() {
            return bytes.length - position;
        }

        boolean hasAvailable() {
            return position < bytes.length;
        }

        /*
         * returns number of skipped bytes
         */
        int skip(int lenToSkip) {
            int newPos = position + lenToSkip;
            if (newPos <= bytes.length) {
                position = newPos;
                return 0;
            } else {
                lenToSkip -= (bytes.length - position);
                position = bytes.length;
                return lenToSkip;
            }
        }

        Node next() {
            if (this.next == null) {
                assert this == tail;
                pullData();
                assert this.next != null;
            }
            return this.next;
        }

        byte peek() {
            return bytes[position];
        }

    }

}

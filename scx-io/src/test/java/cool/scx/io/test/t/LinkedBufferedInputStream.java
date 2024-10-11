package cool.scx.io.test.t;

import java.io.IOException;
import java.io.InputStream;

public class LinkedBufferedInputStream {

    private static final int DEFAULT_BUFFER_SIZE = 2;

    private final InputStream inputStream;
    public Node first; // 头节点指向
    private Node last; // 尾节点指向

    public LinkedBufferedInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.first = new Node(new byte[]{}, 0);
        this.last = this.first;
    }

    private void fillBuffer() {
        try {
            var buffer = new byte[DEFAULT_BUFFER_SIZE];
            var bytesRead = inputStream.read(buffer);
            if (bytesRead > 0) {
                last.next = new Node(buffer, bytesRead);
                last = last.next;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public class Node {
        public final byte[] bytes;
        private final int length;
        private int position;
        private Node next;

        Node(byte[] bytes, int length) {
            this.bytes = bytes;
            this.length = length;
        }

        int available() {
            return length - position;
        }

        boolean hasAvailable() {
            return position < length;
        }

        public Node next() {
            if (this.next == null) {
                fillBuffer();
            }
            return this.next;
        }

        public Node readNext() {
            first = next();
            return first;
        }

        public void setFirst() {
            first = this;
        }

    }

}

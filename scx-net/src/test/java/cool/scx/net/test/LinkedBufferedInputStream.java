package cool.scx.net.test;

import java.io.IOException;
import java.io.InputStream;

public class LinkedBufferedInputStream extends InputStream {

    private final InputStream inputStream;
    private Node first;
    private Node last;
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public LinkedBufferedInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.first = new Node(new byte[]{});
        this.last = this.first;
    }

    private void fillBuffer() {
        try {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead = inputStream.read(buffer);
            if (bytesRead > 0) {
                Node newNode = new Node(buffer, bytesRead);
                last.next = newNode;
                last = newNode;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int read() {
        ensureAvailable();
        if (first == null || !first.hasAvailable()) {
            return -1;
        }
        return first.bytes[first.position++] & 0xFF;
    }

    @Override
    public int read(byte[] b, int off, int len) {
        if (b == null) {
            throw new NullPointerException();
        }
        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }

        int bytesRead = 0;
        while (len > 0) {
            ensureAvailable();
            if (first == null || !first.hasAvailable()) {
                break;
            }
            int toRead = Math.min(len, first.available());
            System.arraycopy(first.bytes, first.position, b, off + bytesRead, toRead);
            first.position += toRead;
            bytesRead += toRead;
            len -= toRead;
        }

        return bytesRead == 0 ? -1 : bytesRead;
    }

    @Override
    public int available() {
        int availableBytes = 0;
        for (Node n = first; n != null; n = n.next) {
            availableBytes += n.available();
        }
        return availableBytes;
    }

    private void ensureAvailable() {
        while (!first.hasAvailable()) {
            if (first.next == null) {
                fillBuffer();
                if (last == first) {
                    break;
                }
            }
            first = first.next;
        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    private static class Node {
        private final byte[] bytes;
        private final int length;
        private int position;
        private Node next;

        Node(byte[] bytes) {
            this(bytes, bytes.length);
        }

        Node(byte[] bytes, int length) {
            this.bytes = bytes;
            this.length=length;
        }

        int available() {
            return length - position;
        }

        boolean hasAvailable() {
            return position < length;
        }
    }

}

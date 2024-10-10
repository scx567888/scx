package cool.scx.net.buffer;

import io.helidon.common.buffers.Bytes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Data reader that can pull additional data.
 */
public class DataReader {
    private final Supplier<byte[]> bytesSupplier;
    private Node head;
    private Node tail;

    /**
     * Data reader from a supplier of bytes.
     *
     * @param bytesSupplier supplier that can be pulled for more data
     */
    public DataReader(Supplier<byte[]> bytesSupplier) {
        this.bytesSupplier = bytesSupplier;
        // we cannot block until data is actually ready to be consumed
        this.head = new Node(new byte[]{});
        this.tail = this.head;
    }

    /**
     * Number of bytes available in the currently pulled data.
     *
     * @return number of bytes available
     */
    public int available() {
        int a = 0;
        for (Node n = head; n != null; n = n.next) {
            a += n.available();
        }
        return a;
    }

    /**
     * Pull next data.
     */
    public void pullData() {
        byte[] bytes = bytesSupplier.get();
        if (bytes == null) {
            throw new InsufficientDataAvailableException();
        }
        Node n = new Node(bytes);
        tail.next = n;
        tail = n;
    }

    /**
     * Skip n bytes.
     *
     * @param lenToSkip number of bytes to skip (must be less or equal to current capacity)
     */
    public void skip(int lenToSkip) {
        while (lenToSkip > 0) {
            ensureAvailable();
            lenToSkip = head.skip(lenToSkip);
        }
    }

    /**
     * Ensure we have at least one byte available.
     */
    // remove consumed head of the list
    // make sure that head has available
    // may block to read
    public void ensureAvailable() {
        while (!head.hasAvailable()) {
            if (head.next == null) {
                pullData();
            }
            head = head.next;
        }
    }

    /**
     * Read 1 byte.
     *
     * @return next byte
     */
    public byte read() {
        ensureAvailable();
        return head.bytes[head.position++];
    }

    /**
     * Look at the next byte (does not modify position).
     *
     * @return next byte
     */
    public byte lookup() {
        ensureAvailable();
        return head.bytes[head.position];
    }

    /**
     * Does the data start with a new line (CRLF).
     *
     * @return whether the data starts with a new line (will pull data to have at least two bytes available)
     */
    public boolean startsWithNewLine() {
        ensureAvailable();
        byte[] bytes = head.bytes;
        int pos = head.position;
        if (bytes[pos] == Bytes.CR_BYTE && ((pos + 1 < bytes.length) ? bytes[pos + 1] : head.next().peek()) == Bytes.LF_BYTE) {
            return true;
        }
        return false;
    }

    /**
     * Does the current data start with the prefix.
     *
     * @param prefix prefix to find, will pull data to have at least prefix.length bytes available
     * @return whether the data starts with the provided prefix
     */
    public boolean startsWith(byte[] prefix) {
        ensureAvailable(); // we have at least 1 byte
        if (prefix.length <= head.available()) { // fast case
            return Arrays.equals(head.bytes, head.position, head.position + prefix.length, prefix, 0, prefix.length);
        } else {
            int offset = 0;
            int remaining = prefix.length;
            for (Node n = head; remaining > 0; n = n.next) {
                int toCmp = Math.min(remaining, n.available());
                if (!Arrays.equals(n.bytes, n.position, n.position + toCmp, prefix, offset, offset + toCmp)) {
                    return false;
                }
                remaining -= toCmp;
                offset += toCmp;
                if (remaining > 0 && n.next == null) {
                    pullData();
                }
            }
            return true;
        }
    }

    /**
     * Read byte array.
     *
     * @param len number of bytes of the string
     * @return string value
     */
    public byte[] readBytes(int len) {
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

    /**
     * Read an ascii string until new line.
     *
     * @return string with the next line
     * @throws io.helidon.common.buffers.DataReader.IncorrectNewLineException when new line cannot be found
     */
    public String readLine() throws IncorrectNewLineException {
        int i = findNewLine(Integer.MAX_VALUE);
        String s = readAsciiString(i);
        skip(2);
        return s;
    }

    private String readAsciiString(int i) {
        return new String(readBytes(i));
    }

    public int findPattern(byte[] pattern, int max){
        ensureAvailable();
        int idx = 0;
        Node n = head;
        int indexWithinNode = n.position;
        int patternIndex = 0;

        while (true) {
            byte[] barr = n.bytes;
            int maxLength = Math.min(max - idx, barr.length - indexWithinNode);

            for (int i = indexWithinNode; i < indexWithinNode + maxLength; i++) {
                if (barr[i] == pattern[patternIndex]) {
                    patternIndex++;
                    if (patternIndex == pattern.length) {
                        return idx + i - n.position - pattern.length + 1;
                    }
                } else {
                    // 回退索引
                    for (int j = patternIndex - 1; j >= 0; j--) {
                        if (barr[i] == pattern[j]) {
                            patternIndex = j + 1;
                            break;
                        }
                        if (j == 0) {
                            patternIndex = 0;
                        }
                    }
                }
            }

            idx += maxLength;
            if (idx >= max) {
                return max;
            }
            n = n.next();
            indexWithinNode = n.position;
        }
    }


        /**
         * Find new line with the next n bytes.
         *
         * @param max length to search
         * @return index of the new line, or max if not found
         * @throws io.helidon.common.buffers.DataReader.IncorrectNewLineException in case there is a LF without CR,
         *              or CR without a LF
         */
    public int findNewLine(int max) throws IncorrectNewLineException {
        ensureAvailable();
        int idx = 0;
        Node n = head;
        int indexWithinNode = n.position;

        while (true) {
            byte[] barr = n.bytes;
            int maxLength = Math.min(max - idx, barr.length - indexWithinNode);
            int crIndex = Bytes.firstIndexOf(barr, indexWithinNode, indexWithinNode + maxLength, Bytes.CR_BYTE);

            if (crIndex == -1) {
                int lfIndex = Bytes.firstIndexOf(barr, indexWithinNode, indexWithinNode + maxLength, Bytes.LF_BYTE);
                if (lfIndex != -1) {
                  
                }
                // not found, continue with next buffer
                idx += maxLength;
                if (idx >= max) {
                    // not found and reached the limit
                    return max;
                }
                n = n.next();
                indexWithinNode = n.position;
                continue;
            } else {
                // found, next byte should be LF
                if (crIndex == barr.length - 1) {
                    // found CR as the last byte of the current node, peek next node
                    byte nextByte = n.next().peek();
                    if (nextByte == Bytes.LF_BYTE) {
                        return idx + crIndex - n.position;
                    }
                } else {
                    // found CR within the current array
                    byte nextByte = barr[crIndex + 1];
                    if (nextByte == Bytes.LF_BYTE) {
                        return idx + crIndex - n.position;
                    }
                    indexWithinNode = crIndex + 1;
                    idx += indexWithinNode;
                    if (idx >= max) {
                        return max;
                    }
                    continue;
                }
            }

            idx += maxLength;
            if (idx >= max) {
                return max;
            }
            n = n.next();
            indexWithinNode = n.position;
        }
    }

    /**
     * New line not valid.
     */
    public static class IncorrectNewLineException extends RuntimeException {
        /**
         * Incorrect new line.
         *
         * @param message descriptive message
         */
        public IncorrectNewLineException(String message) {
            super(message);
        }
    }

    /**
     * Not enough data available to finish the requested operation.
     */
    public static class InsufficientDataAvailableException extends RuntimeException {
        /**
         * Create a new instance. This exception does not have any other constructors.
         */
        public InsufficientDataAvailableException() {
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

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));
        DataReader d=new DataReader(()->{
            try {
                return inputStream.readNBytes(1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        for (int i = 0; i < 10; ) {
            var bytes = d.findNewLine(Integer.MAX_VALUE);
            var bytes1 = d.findPattern("\r\n".getBytes(),Integer.MAX_VALUE);
            System.out.println(bytes);
        }
        
    }
    
}

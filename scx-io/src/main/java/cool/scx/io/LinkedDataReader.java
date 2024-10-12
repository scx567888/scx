package cool.scx.io;

import java.util.Arrays;
import java.util.function.Supplier;

//todo 实现所有功能
public class LinkedDataReader implements DataReader {

    private final Supplier<byte[]> bytesSupplier;
    private Node head;
    private Node tail;

    public LinkedDataReader(Supplier<byte[]> bytesSupplier) {
        this.bytesSupplier = bytesSupplier;
        this.head = new Node(EMPTY_BYTES);
        this.tail = this.head;
    }

    public boolean pullData() {
        var bytes = bytesSupplier.get();
        if (bytes == null) {
            return false;
        }
        tail.next = new Node(bytes);
        tail = tail.next;
        return true;
    }

    public void ensureAvailable() {
        while (!head.hasAvailable()) {
            if (head.next == null) {
                var b = pullData();
                if (!b) {
                    throw new NoMoreDataException();
                }
            }
            head = head.next;
        }
    }

    @Override
    public byte read() throws NoMoreDataException {
        ensureAvailable();
        return head.bytes[head.position++];
    }

    @Override
    public byte[] read(int maxLength) throws NoMoreDataException {
        ensureAvailable();
        
        byte[] result = new byte[maxLength];
        int bytesRead = 0;
        
        while (bytesRead < maxLength) {
            int available = head.available();
            int lengthToRead = Math.min(available, maxLength - bytesRead);
            System.arraycopy(head.bytes, head.position, result, bytesRead, lengthToRead);
            head.position += lengthToRead;
            bytesRead += lengthToRead;

            if (!head.hasAvailable()) {
                if (head.next == null) {
                    var b = pullData();
                    if (!b) {
                        break;
                    }
                }
                head = head.next();
            }
            
        }
        return bytesRead == maxLength ? result : Arrays.copyOf(result, bytesRead);
    }

    @Override
    public byte get() throws NoMoreDataException {
        ensureAvailable();
        return head.bytes[head.position];
    }

    @Override
    public byte[] get(int maxLength) throws NoMoreDataException {
        ensureAvailable();
        
        byte[] result = new byte[maxLength];
        int bytesRead = 0;
        
        Node currentNode = head;
        int currentPosition = head.position;

        while (bytesRead < maxLength) {
            int available = currentNode.bytes.length - currentPosition;
            int lengthToRead = Math.min(available, maxLength - bytesRead);
            System.arraycopy(currentNode.bytes, currentPosition, result, bytesRead, lengthToRead);
            currentPosition += lengthToRead;
            bytesRead += lengthToRead;

            if (currentPosition >= currentNode.bytes.length) {
                if (currentNode.next == null ) {
                    var b= pullData();
                    if (!b) {
                        break;
                    }
                }
                currentNode = currentNode.next();
                currentPosition = currentNode.position;
            }

        }

        return bytesRead == maxLength ? result : Arrays.copyOf(result, bytesRead);
    }


    @Override
    public int indexOf(byte b) throws NoMatchFoundException {
        return 0;
    }

    @Override
    public int indexOf(byte[] b) throws NoMatchFoundException {
        return 0;
    }

    @Override
    public void skip(int length) {

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

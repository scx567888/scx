package cool.scx.byte_reader;

/// ByteNode
///
/// @author scx567888
/// @version 0.0.1
public class ByteNode {

    public final byte[] bytes;
    public final int startPosition;
    public final int endPositon;
    public int position;
    public ByteNode next;

    public ByteNode(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    public ByteNode(byte[] bytes, int startPosition, int endPositon) {
        this.bytes = bytes;
        this.startPosition = startPosition;
        this.endPositon = endPositon;
        this.position = startPosition;
    }

    public int available() {
        return endPositon - position;
    }

    public boolean hasAvailable() {
        return position < endPositon;
    }

    public void reset() {
        position = startPosition;
    }

    @Override
    public String toString() {
        return new String(bytes, position, endPositon - position);
    }

}

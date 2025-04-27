package cool.scx.io.data_node;

/// DataNode
///
/// @author scx567888
/// @version 0.0.1
public class DataNode {

    public final byte[] bytes;
    public final int start;
    public final int limit;
    public int position;
    public DataNode next;

    public DataNode(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    public DataNode(byte[] bytes, int start, int limit) {
        this.bytes = bytes;
        this.start = start;
        this.limit = limit;
        this.position = start;
    }

    public int available() {
        return limit - position;
    }

    public boolean hasAvailable() {
        return position < limit;
    }

    public void reset() {
        position = start;
    }

    @Override
    public String toString() {
        return new String(bytes, position, limit - position);
    }

}

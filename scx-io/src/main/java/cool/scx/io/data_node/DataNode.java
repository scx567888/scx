package cool.scx.io.data_node;

/// DataNode
///
/// @author scx567888
/// @version 0.0.1
public class DataNode {

    public final byte[] bytes;
    public final int limit;
    public int position;
    public DataNode next;

    public DataNode(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    public DataNode(byte[] bytes, int position, int limit) {
        this.bytes = bytes;
        this.position = position;
        this.limit = limit;
    }

    public int available() {
        return limit - position;
    }

    public boolean hasAvailable() {
        return position < limit;
    }

    @Override
    public String toString() {
        return new String(bytes, position, limit - position);
    }

}

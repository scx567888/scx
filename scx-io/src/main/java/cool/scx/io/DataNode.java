package cool.scx.io;

/**
 * DataNode
 *
 * @author scx567888
 * @version 0.0.1
 */
public class DataNode {

    final byte[] bytes;
    final int limit;
    int position;
    DataNode next;

    public DataNode(byte[] bytes) {
        this.bytes = bytes;
        this.position = 0;
        this.limit = bytes.length;
    }

    public DataNode(byte[] bytes, int position, int limit) {
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

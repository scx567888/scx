package cool.scx.byte_reader;

/// ByteNode
///
/// @author scx567888
/// @version 0.0.1
public class ByteNode {

    public final ByteChunk chunk;
    /// 相对 索引 0 起始
    public int position;
    public ByteNode next;

    public ByteNode(ByteChunk chunk) {
        this.chunk = chunk;
        this.position = 0;
    }

    public int available() {
        return chunk.length - position;
    }

    public boolean hasAvailable() {
        return position < chunk.length;
    }

    public void reset() {
        position = 0;
    }

    @Override
    public String toString() {
        return chunk.toString(position, chunk.length);
    }

}

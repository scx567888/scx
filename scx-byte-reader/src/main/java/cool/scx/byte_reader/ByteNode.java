package cool.scx.byte_reader;

/// ByteNode
///
/// @author scx567888
/// @version 0.0.1
public class ByteNode {

    public final ByteChunk chunk;
    public int position;
    public ByteNode next;

    public ByteNode(ByteChunk chunk) {
        this.chunk = chunk;
        this.position = chunk.startPosition;
    }

    public int available() {
        return chunk.endPosition - position;
    }

    public boolean hasAvailable() {
        return position < chunk.endPosition;
    }

    public void reset() {
        position = chunk.startPosition;
    }

    @Override
    public String toString() {
        return new String(chunk.bytes, position, chunk.endPosition - position);
    }

}

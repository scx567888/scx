package cool.scx.byte_reader;

/// ByteChunk
public final class ByteChunk {

    public final byte[] bytes;
    public final int startPosition;
    public final int endPosition;
    public final int length;

    public ByteChunk(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    public ByteChunk(byte[] bytes, int startPosition, int endPosition) {
        this.bytes = bytes;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.length = endPosition - startPosition;
    }

    @Override
    public String toString() {
        return new String(bytes, startPosition, length);
    }

}

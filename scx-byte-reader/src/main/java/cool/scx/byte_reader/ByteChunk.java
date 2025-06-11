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

    /// 相对 索引 0 起始
    public byte getByte(int index) {
        return bytes[startPosition + index];
    }

    /// 相对 索引 0 起始
    public byte[] getBytes(int start, int end) {
        if (start == 0 && end == length && startPosition == 0 && length == bytes.length) {
            return bytes;
        }
        var data = new byte[end - start];
        System.arraycopy(bytes, startPosition + start, data, 0, data.length);
        return data;
    }

    /// 相对 索引 0 起始
    public byte[] getBytes(int start) {
        return getBytes(start, length);
    }

    /// 相对 索引 0 起始
    public byte[] getBytes() {
        return getBytes(0, length);
    }

    /// 相对 索引 0 起始
    public String toString(int start, int end) {
        return new String(bytes, startPosition + start, end - start);
    }

    /// 相对 索引 0 起始
    public String toString(int start) {
        return toString(start, length);
    }

    @Override
    public String toString() {
        return toString(0, length);
    }

    /// 相对 索引 0 起始
    public ByteChunk splice(int start, int end) {
        if (start == 0 && end == length) {
            return this;
        }
        return new ByteChunk(bytes, startPosition + start, startPosition + end);
    }

}

package cool.scx.byte_reader.consumer;

import cool.scx.byte_reader.ByteChunk;

/// 填充方式
///
/// @author scx567888
/// @version 0.0.1
public class FillByteArrayByteConsumer implements ByteConsumer {

    private final byte[] data;
    private final int dataPosition;
    private final int dataLength;
    private int i;

    public FillByteArrayByteConsumer(byte[] data, int position, int length) {
        this.data = data;
        this.dataPosition = position;
        this.dataLength = length;
        this.i = this.dataPosition;
    }

    @Override
    public boolean accept(ByteChunk byteChunk) {
        if (i + byteChunk.length > dataLength) {
            throw new IllegalStateException("Buffer overflow: not enough space to accept more data");
        }
        System.arraycopy(byteChunk.bytes, byteChunk.startPosition, data, i, byteChunk.length);
        i += byteChunk.length;
        return true;
    }

    public int getFilledLength() {
        return i - dataPosition;
    }

}

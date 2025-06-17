package cool.scx.bytes.consumer;

import cool.scx.bytes.ByteChunk;

/// 填充方式
///
/// @author scx567888
/// @version 0.0.1
public class FillByteArrayByteConsumer implements ByteConsumer {

    private final byte[] data;
    private final int position;
    private final int length;
    private int filledLength;//写入的数量

    public FillByteArrayByteConsumer(byte[] data, int position, int length) {
        this.data = data;
        this.position = position;
        this.length = length;
        this.filledLength = 0;
    }

    @Override
    public boolean accept(ByteChunk byteChunk) {
        if (filledLength + byteChunk.length > length) {
            throw new IllegalStateException("Buffer overflow: not enough space to accept more data");
        }
        System.arraycopy(byteChunk.bytes, byteChunk.start, data, position + filledLength, byteChunk.length);
        filledLength += byteChunk.length;
        return true;
    }

    public int getFilledLength() {
        return filledLength;
    }

}

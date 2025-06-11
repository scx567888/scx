package cool.scx.byte_reader.consumer;

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
    public boolean accept(byte[] bytes, int position, int length) {
        if (i + length > dataLength) {
            throw new IllegalStateException("Buffer overflow: not enough space to accept more data");
        }
        System.arraycopy(bytes, position, data, i, length);
        i += length;
        return true;
    }

    public int getFilledLength() {
        return i - dataPosition;
    }

}

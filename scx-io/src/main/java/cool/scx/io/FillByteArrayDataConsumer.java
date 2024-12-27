package cool.scx.io;

/**
 * 填充方式
 */
public class FillByteArrayDataConsumer implements DataConsumer {

    private final byte[] data;
    private final int dataPosition;
    private final int dataLength;
    private int i;

    public FillByteArrayDataConsumer(byte[] data, int position, int length) {
        this.data = data;
        this.dataPosition = position;
        this.dataLength = length;
        this.i = this.dataPosition;
    }

    @Override
    public void accept(byte[] bytes, int position, int length) {
        if (i + length > dataLength) {
            throw new IllegalStateException("Buffer overflow: not enough space to accept more data");
        }
        System.arraycopy(bytes, position, data, i, length);
        i += length;
    }

    public int getFilledLength() {
        return i - dataPosition;
    }

}

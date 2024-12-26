package cool.scx.io;

import java.nio.ByteBuffer;

/**
 * 填充方式
 */
public class FillByteBufferDataConsumer implements DataConsumer {

    private final ByteBuffer data;
    private final int dataPosition;

    public FillByteBufferDataConsumer(ByteBuffer data) {
        this.data = data;
        this.dataPosition = data.position();
    }

    @Override
    public void accept(byte[] bytes, int position, int length) {
        if (data.remaining() < length) {
            throw new IllegalStateException("Buffer overflow: not enough space to accept more data");
        }
        data.put(bytes, position, length);
    }

    public int getFilledLength() {
        return data.position() - dataPosition;
    }

}

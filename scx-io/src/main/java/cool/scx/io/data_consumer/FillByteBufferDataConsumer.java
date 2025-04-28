package cool.scx.io.data_consumer;

import java.nio.ByteBuffer;

/// 填充方式
public class FillByteBufferDataConsumer implements DataConsumer {

    private final ByteBuffer data;
    private final int dataPosition;

    public FillByteBufferDataConsumer(ByteBuffer data) {
        this.data = data;
        this.dataPosition = data.position();
    }

    @Override
    public boolean accept(byte[] bytes, int position, int length) {
        if (data.remaining() < length) {
            throw new IllegalStateException("Buffer overflow: not enough space to accept more data");
        }
        data.put(bytes, position, length);
        return true;
    }

    public int getFilledLength() {
        return data.position() - dataPosition;
    }

}

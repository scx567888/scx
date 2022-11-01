package cool.scx.core.eventbus;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * 零拷贝编解码器 (因为并不适用于集群模式,所以此处不实现 encodeToWire和decodeFromWire)
 */
public class ZeroCopyMessageCodec implements MessageCodec<Object, Object> {

    public static final String ZERO_COPY_CODEC_NAME = ZeroCopyMessageCodec.class.getName();

    public static final ZeroCopyMessageCodec DEFAULT_INSTANCE = new ZeroCopyMessageCodec();

    @Override
    public void encodeToWire(Buffer buffer, Object o) {
        throw new UnsupportedOperationException("集群模式下无法使用 ZeroCopyMessageCodec");
    }

    @Override
    public Object decodeFromWire(int pos, Buffer buffer) {
        throw new UnsupportedOperationException("集群模式下无法使用 ZeroCopyMessageCodec");
    }

    @Override
    public Object transform(Object o) {
        return o;
    }

    @Override
    public String name() {
        return ZERO_COPY_CODEC_NAME;
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }

}

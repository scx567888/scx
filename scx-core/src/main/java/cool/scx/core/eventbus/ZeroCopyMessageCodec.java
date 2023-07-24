package cool.scx.core.eventbus;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;

import java.util.function.Function;

/**
 * 零拷贝编解码器 (因为并不适用于集群模式,所以此处不实现 encodeToWire和decodeFromWire)
 *
 * @author scx567888
 * @version 1.18.0
 */
public class ZeroCopyMessageCodec implements MessageCodec<Object, Object> {

    /**
     * Constant <code>ZERO_COPY_CODEC_NAME="ZeroCopyMessageCodec.class.getName()"</code>
     */
    public static final String ZERO_COPY_CODEC_NAME = ZeroCopyMessageCodec.class.getName();

    private static final ZeroCopyMessageCodec DEFAULT_INSTANCE = new ZeroCopyMessageCodec();

    private static final Function<Object, String> CODEC_SELECTOR = o -> {
        var zeroCopyMessage = o.getClass().getAnnotation(ZeroCopyMessage.class);
        return zeroCopyMessage != null ? ZERO_COPY_CODEC_NAME : null;
    };

    /**
     * <p>registerCodec.</p>
     *
     * @param eventBus a {@link io.vertx.core.eventbus.EventBus} object
     */
    public static void registerCodec(EventBus eventBus) {
        eventBus.registerCodec(DEFAULT_INSTANCE);
        eventBus.codecSelector(CODEC_SELECTOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeToWire(Buffer buffer, Object o) {
        throw new UnsupportedOperationException("集群模式下无法使用 ZeroCopyMessageCodec");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object decodeFromWire(int pos, Buffer buffer) {
        throw new UnsupportedOperationException("集群模式下无法使用 ZeroCopyMessageCodec");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object transform(Object o) {
        return o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return ZERO_COPY_CODEC_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte systemCodecID() {
        return -1;
    }

}

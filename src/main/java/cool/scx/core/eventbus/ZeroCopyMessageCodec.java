package cool.scx.core.eventbus;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;

/**
 * 零拷贝编解码器 (因为并不适用于集群模式,所以此处不实现 encodeToWire和decodeFromWire)
 *
 * @author scx567888
 * @version 1.18.0
 */
public final class ZeroCopyMessageCodec<T> implements MessageCodec<T, Object> {

    /**
     * ZERO_COPY_CODEC_NAME
     */
    public static final String ZERO_COPY_CODEC_NAME = ZeroCopyMessageCodec.class.getName();

    /**
     * <p>registerCodec.</p>
     *
     * @param eventBus a {@link io.vertx.core.eventbus.EventBus} object
     */
    public static void registerCodec(EventBus eventBus) {
        eventBus.registerDefaultCodec(ZeroCopyMessageWrapper.class, new ZeroCopyMessageCodec<>());
        eventBus.codecSelector(o -> {
            var zeroCopyMessage = o.getClass().getAnnotation(ZeroCopyMessage.class);
            return zeroCopyMessage != null ? ZERO_COPY_CODEC_NAME : null;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeToWire(Buffer buffer, T o) {
        throw new UnsupportedOperationException("集群模式下无法使用 ZeroCopyMessageCodec !!!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T decodeFromWire(int pos, Buffer buffer) {
        throw new UnsupportedOperationException("集群模式下无法使用 ZeroCopyMessageCodec !!!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object transform(T o) {
        //针对 ZeroCopyMessageWrapper 进行脱壳
        if (o instanceof ZeroCopyMessageWrapper<?> z) {
            return z.message();
        }
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

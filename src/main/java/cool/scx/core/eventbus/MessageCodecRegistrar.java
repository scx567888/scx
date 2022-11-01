package cool.scx.core.eventbus;

import io.vertx.core.eventbus.EventBus;

/**
 * <p>MessageCodecRegistrar class.</p>
 *
 * @author scx567888
 * @version $Id: $Id
 */
public class MessageCodecRegistrar {

    /**
     * <p>registerCodec.</p>
     *
     * @param eventBus a {@link io.vertx.core.eventbus.EventBus} object
     */
    public static void registerCodec(EventBus eventBus) {
        eventBus.registerCodec(ZeroCopyMessageCodec.DEFAULT_INSTANCE);
    }

}

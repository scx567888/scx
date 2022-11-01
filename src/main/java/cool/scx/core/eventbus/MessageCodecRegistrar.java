package cool.scx.core.eventbus;

import io.vertx.core.eventbus.EventBus;

public class MessageCodecRegistrar {

    public static void registerCodec(EventBus eventBus) {
        eventBus.registerCodec(ZeroCopyMessageCodec.DEFAULT_INSTANCE);
    }

}

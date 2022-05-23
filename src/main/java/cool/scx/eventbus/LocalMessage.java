package cool.scx.eventbus;

import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.impl.EventBusImpl;
import io.vertx.core.eventbus.impl.MessageImpl;

/**
 * 本地消息
 *
 * @author scx567888
 * @version 1.7.3
 */
final class LocalMessage extends MessageImpl<Object, Object> {

    /**
     * <p>Constructor for LocalMessage.</p>
     *
     * @param address  a {@link java.lang.String} object
     * @param headers  a {@link io.vertx.core.MultiMap} object
     * @param sentBody a {@link java.lang.Object} object
     * @param send     a boolean
     * @param bus      a {@link io.vertx.core.eventbus.impl.EventBusImpl} object
     */
    public LocalMessage(String address, MultiMap headers, Object sentBody, boolean send, EventBusImpl bus) {
        super(address, headers, sentBody, null, send, bus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object body() {
        return this.sentBody;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalMessage copyBeforeReceive() {
        return this;
    }

}

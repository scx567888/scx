package cool.scx.eventbus;

import cool.scx.ScxHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.impl.EventBusImpl;

import java.util.Objects;

/**
 * 事件总线<br>
 * 针对 vertx 的 eventbus 进行简单封装
 *
 * @author scx567888
 * @version 1.1.9
 */
public final class ScxEventBus {

    /**
     * vertx 的事件总线
     */
    private final EventBusImpl vertxEventBus;

    /**
     * a
     *
     * @param vertx v
     */
    public ScxEventBus(Vertx vertx) {
        this.vertxEventBus = (EventBusImpl) vertx.eventBus();
    }

    /**
     * 注册消费者 这里注册的消费者 前端和本地均可以进行调用
     *
     * @param address a
     * @param handler h
     */
    public void consumer(String address, ScxHandler<Object> handler) {
        vertxEventBus.localConsumer(address, message -> handler.handle(message.body()));
    }

    /**
     * <p>send.</p>
     *
     * @param address a {@link java.lang.String} object
     * @param message a {@link java.lang.Object} object
     */
    public void send(String address, Object message) {
        _sendOrPubInternal(true, address, message);
    }

    /**
     * <p>publish.</p>
     *
     * @param address a {@link java.lang.String} object
     * @param message a {@link java.lang.Object} object
     */
    public void publish(String address, Object message) {
        _sendOrPubInternal(false, address, message);
    }

    /**
     * a
     *
     * @param send    a
     * @param address a
     * @param message a
     */
    private void _sendOrPubInternal(boolean send, String address, Object message) {
        DeliveryOptions options = new DeliveryOptions();
        Objects.requireNonNull(address, "no null address accepted");
        vertxEventBus.sendOrPubInternal(new LocalMessage(address, options.getHeaders(), message, send, vertxEventBus), options, null, null);
    }

}

package cool.scx.app.eventbus;

import cool.scx.common.multi_map.MultiMap;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventBus {

    private final MultiMap<String, Consumer<Object>> events = new MultiMap<>(ConcurrentHashMap::new, ArrayList::new);

    public void publish(String name, Object data) {
        var consumers = events.getAll(name);
        //这里暂时并行处理
        for (var consumer : consumers) {
            Thread.ofVirtual().start(() -> consumer.accept(data));
        }
    }

    public void consumer(String name, Consumer<Object> consumer) {
        this.events.add(name, consumer);
    }

}

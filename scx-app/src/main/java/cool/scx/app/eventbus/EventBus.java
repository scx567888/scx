package cool.scx.app.eventbus;

import dev.scx.collections.multi_map.MultiMap;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class EventBus {

    private final MultiMap<String, Consumer<Object>> events = new MultiMap<>(ConcurrentHashMap::new, ArrayList::new);
    private final Executor executor;

    public EventBus(Executor executor) {
        this.executor = executor;
    }

    public void publish(String name, Object data) {
        var consumers = events.getAll(name);
        for (var consumer : consumers) {
            executor.execute(() -> consumer.accept(data));
        }
    }

    public void consumer(String name, Consumer<Object> consumer) {
        this.events.add(name, consumer);
    }

}

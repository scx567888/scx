package cool.scx.http.media.event_stream.event;

import cool.scx.http.media.event_stream.ClientEventStream;
import cool.scx.http.media.event_stream.SseEvent;

import java.util.function.Consumer;

public class EventClientEventStream {

    private final ClientEventStream eventStream;
    private Consumer<SseEvent> eventHandler;
    private boolean running;

    public EventClientEventStream(ClientEventStream eventStream) {
        this.eventStream = eventStream;
        this.running = false;
    }

    public static EventClientEventStream of(ClientEventStream eventStream) {
        return new EventClientEventStream(eventStream);
    }

    // 注册事件处理器
    public EventClientEventStream onEvent(Consumer<SseEvent> eventHandler) {
        this.eventHandler = eventHandler;
        return this;
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        while (running) {
            try {
                var event = eventStream.readEvent();
                _callEventHandler(event);
            } catch (Exception e) {
                // 处理 IO 异常, 可能是连接关闭
                break;
            }
        }
    }

    // 调用事件处理器
    private void _callEventHandler(SseEvent sseEvent) {
        if (eventHandler != null) {
            eventHandler.accept(sseEvent);
        }
    }

    // 停止事件流
    public void stop() {
        this.running = false;
    }

}

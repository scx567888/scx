package cool.scx.http.media.event_stream;

public interface SseEvent {

    static SseEventWritable of() {
        return new SseEventImpl();
    }

    static SseEventWritable of(String data) {
        return new SseEventImpl().data(data);
    }

    // 事件类型
    String event();

    // 事件数据
    String data();

    // 事件 ID
    String id();

    // 重试时间
    Long retry();

    // 可选的评论
    String comment();

}

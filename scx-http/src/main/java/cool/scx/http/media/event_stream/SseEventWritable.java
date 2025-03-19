package cool.scx.http.media.event_stream;

public interface SseEventWritable extends SseEvent {

    // 事件类型
    SseEventWritable event(String event);

    // 事件数据
    SseEventWritable data(String data);

    // 事件 ID
    SseEventWritable id(String id);

    // 重试时间
    SseEventWritable retry(Long retry);

    // 可选的评论
    SseEventWritable comment(String comment);

}

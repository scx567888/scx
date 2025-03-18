package cool.scx.http.media.event_stream;

public class SseEventImpl implements SseEventWritable {

    private String event;
    private String data;
    private String id;
    private Long retry;
    private String comment;

    @Override
    public SseEventWritable event(String event) {
        this.event = event;
        return this;
    }

    @Override
    public SseEventWritable data(String data) {
        this.data = data;
        return this;
    }

    @Override
    public SseEventWritable id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public SseEventWritable retry(Long retry) {
        this.retry = retry;
        return this;
    }

    @Override
    public SseEventWritable comment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public String event() {
        return event;
    }

    @Override
    public String data() {
        return data;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Long retry() {
        return retry;
    }

    @Override
    public String comment() {
        return comment;
    }

}

package cool.scx.http.x.http1;

public class Http1ServerConnectionOptions {

    private int maxRequestLineSize;// 最大请求行大小
    private int maxHeaderSize;// 最大请求头大小
    private long maxPayloadSize;// 最大请求体大小
    private boolean autoRespond100Continue;// 自动响应 100-continue

    public Http1ServerConnectionOptions() {
        this.maxRequestLineSize = 1024 * 64; // 默认 64 KB
        this.maxHeaderSize = 1024 * 128; // 默认 128 KB
        this.maxPayloadSize = 1024 * 1024 * 16; // 默认 16 MB
        this.autoRespond100Continue = true;// 默认自动响应
    }

    public Http1ServerConnectionOptions(Http1ServerConnectionOptions oldOptions) {
        maxRequestLineSize(oldOptions.maxRequestLineSize());
        maxHeaderSize(oldOptions.maxHeaderSize());
        maxPayloadSize(oldOptions.maxPayloadSize());
        autoRespond100Continue(oldOptions.autoRespond100Continue());
    }

    public int maxRequestLineSize() {
        return maxRequestLineSize;
    }

    public Http1ServerConnectionOptions maxRequestLineSize(int maxRequestLineSize) {
        this.maxRequestLineSize = maxRequestLineSize;
        return this;
    }

    public int maxHeaderSize() {
        return maxHeaderSize;
    }

    public Http1ServerConnectionOptions maxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
        return this;
    }

    public long maxPayloadSize() {
        return maxPayloadSize;
    }

    public Http1ServerConnectionOptions maxPayloadSize(long maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }

    public boolean autoRespond100Continue() {
        return autoRespond100Continue;
    }

    public Http1ServerConnectionOptions autoRespond100Continue(boolean autoRespond100Continue) {
        this.autoRespond100Continue = autoRespond100Continue;
        return this;
    }
}

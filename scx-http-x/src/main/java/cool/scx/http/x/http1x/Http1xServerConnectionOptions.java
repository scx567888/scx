package cool.scx.http.x.http1x;

public class Http1xServerConnectionOptions {

    private int maxRequestLineSize;// 最大请求行大小
    private int maxHeaderSize;// 最大请求头大小
    private long maxPayloadSize;// 最大请求体大小

    public Http1xServerConnectionOptions() {
        this.maxRequestLineSize = 1024 * 64; // 默认 64 KB
        this.maxHeaderSize = 1024 * 128; // 默认 128 KB
        this.maxPayloadSize = 1024 * 1024 * 16; // 默认 16 MB
    }

    public Http1xServerConnectionOptions(Http1xServerConnectionOptions oldOptions) {
        maxRequestLineSize(oldOptions.maxRequestLineSize());
        maxHeaderSize(oldOptions.maxHeaderSize());
        maxPayloadSize(oldOptions.maxPayloadSize());
    }

    public int maxRequestLineSize() {
        return maxRequestLineSize;
    }

    public Http1xServerConnectionOptions maxRequestLineSize(int maxRequestLineSize) {
        this.maxRequestLineSize = maxRequestLineSize;
        return this;
    }

    public int maxHeaderSize() {
        return maxHeaderSize;
    }

    public Http1xServerConnectionOptions maxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
        return this;
    }

    public long maxPayloadSize() {
        return maxPayloadSize;
    }

    public Http1xServerConnectionOptions maxPayloadSize(long maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }

}

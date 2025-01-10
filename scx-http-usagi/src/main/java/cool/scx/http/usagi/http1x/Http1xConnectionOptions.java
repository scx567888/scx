package cool.scx.http.usagi.http1x;

public class Http1xConnectionOptions {

    private int maxRequestLineSize;// 最大请求行大小
    private int maxHeaderSize;// 最大请求头大小
    private int maxPayloadSize;// 最大请求体大小

    public Http1xConnectionOptions() {
        this.maxRequestLineSize = 1024 * 64; // 默认 64 KB
        this.maxHeaderSize = 1024 * 128; // 默认 128 KB
        this.maxPayloadSize = 1024 * 1024 * 16; // 默认 16 MB
    }

    public Http1xConnectionOptions(Http1xConnectionOptions oldOptions) {
        maxRequestLineSize(oldOptions.maxRequestLineSize());
        maxHeaderSize(oldOptions.maxHeaderSize());
        maxPayloadSize(oldOptions.maxPayloadSize());
    }

    public int maxRequestLineSize() {
        return maxRequestLineSize;
    }

    public Http1xConnectionOptions maxRequestLineSize(int maxRequestLineSize) {
        this.maxRequestLineSize = maxRequestLineSize;
        return this;
    }

    public int maxHeaderSize() {
        return maxHeaderSize;
    }

    public Http1xConnectionOptions maxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
        return this;
    }

    public int maxPayloadSize() {
        return maxPayloadSize;
    }

    public Http1xConnectionOptions maxPayloadSize(int maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }
    
}

package cool.scx.http.x.http1x;

public class Http1xClientConnectionOptions {

    private int maxStatusLineSize;// 最大响应行大小
    private int maxHeaderSize;// 最大请求头大小
    private int maxPayloadSize;// 最大请求体大小

    public Http1xClientConnectionOptions() {
        this.maxStatusLineSize = 1024 * 64; // 默认 64 KB
        this.maxHeaderSize = 1024 * 128; // 默认 128 KB
        this.maxPayloadSize = 1024 * 1024 * 16; // 默认 16 MB
    }

    public Http1xClientConnectionOptions(Http1xClientConnectionOptions oldOptions) {
        maxStatusLineSize(oldOptions.maxStatusLineSize());
        maxHeaderSize(oldOptions.maxHeaderSize());
        maxPayloadSize(oldOptions.maxPayloadSize());
    }

    public int maxStatusLineSize() {
        return maxStatusLineSize;
    }

    public Http1xClientConnectionOptions maxStatusLineSize(int maxStatusLineSize) {
        this.maxStatusLineSize = maxStatusLineSize;
        return this;
    }

    public int maxHeaderSize() {
        return maxHeaderSize;
    }

    public Http1xClientConnectionOptions maxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
        return this;
    }

    public int maxPayloadSize() {
        return maxPayloadSize;
    }

    public Http1xClientConnectionOptions maxPayloadSize(int maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }

}

package cool.scx.http.x.http2;

/// @see <a href="https://www.rfc-editor.org/rfc/rfc9113.html#name-frame-definitions">https://www.rfc-editor.org/rfc/rfc9113.html#name-frame-definitions</a>
public enum Http2FrameType {

    DATA(0x00),
    HEADERS(0x01),
    PRIORITY(0x02),
    RST_STREAM(0x03),
    SETTINGS(0x04),
    PUSH_PROMISE(0x05),
    PING(0x06),
    GOAWAY(0x07),
    WINDOW_UPDATE(0x08),
    CONTINUATION(0x09);

    /// 存储 code 和 对应枚举的映射
    private static final Http2FrameType[] MAP = initMap();

    private final byte code;

    Http2FrameType(int code) {
        this.code = (byte) code;
    }

    private static Http2FrameType[] initMap() {
        var m = new Http2FrameType[10];
        var values = Http2FrameType.values();
        for (var v : values) {
            m[v.code] = v;
        }
        return m;
    }

    public static Http2FrameType of(int code) {
        if (code < 0 || code > 9) {
            throw new IllegalArgumentException("Invalid Http2FrameType : " + code);
        }
        var c = MAP[code];
        if (c == null) {
            throw new IllegalArgumentException("Invalid Http2FrameType : " + code);
        }
        return c;
    }

    public static Http2FrameType find(int code) {
        if (code < 0 || code > 9) {
            return null;
        }
        return MAP[code];
    }

    public int code() {
        return code;
    }

}

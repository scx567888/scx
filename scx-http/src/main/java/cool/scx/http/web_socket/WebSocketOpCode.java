package cool.scx.http.web_socket;

public enum WebSocketOpCode {

    /**
     * Continuation Frames
     */
    CONTINUATION(0x0),

    /**
     * Text Data Frames
     */
    TEXT(0x1),

    /**
     * Binary Data Frames
     */
    BINARY(0x2),

    /**
     * Close Control Frames
     */
    CLOSE(0x8),

    /**
     * Ping Control Frames
     */
    PING(0x9),

    /**
     * Pong Control Frames
     */
    PONG(0xA);

    private static final WebSocketOpCode[] MAP = initMap();

    private final int code;

    WebSocketOpCode(int code) {
        this.code = code;
    }

    private static WebSocketOpCode[] initMap() {
        var m = new WebSocketOpCode[11];
        var values = WebSocketOpCode.values();
        for (var v : values) {
            m[v.code] = v;
        }
        return m;
    }

    /**
     * @param code c
     * @return 未找到时 抛出异常
     */
    public static WebSocketOpCode of(int code) {
        if (code < 0 || code > 10) {
            throw new IllegalArgumentException("Invalid WebSocket OpCode: " + code);
        }
        var c = MAP[code];
        if (c == null) {
            throw new IllegalArgumentException("Invalid WebSocket OpCode: " + code);
        }
        return c;
    }

    /**
     * @param code c
     * @return 未找到时 返回 null
     */
    public static WebSocketOpCode find(int code) {
        if (code < 0 || code > 10) {
            return null;
        }
        return MAP[code];
    }

    public int code() {
        return code;
    }

}

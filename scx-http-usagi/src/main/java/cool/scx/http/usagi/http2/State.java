package cool.scx.http.usagi.http2;

enum State {
    READ_FRAME,
    DATA,
    HEADERS,
    PRIORITY,
    RST_STREAM,
    SETTINGS,
    READ_PUSH_PROMISE,
    PING,
    GO_AWAY,
    WINDOW_UPDATE,
    ACK_SETTINGS,
    WRITE_SERVER_SETTINGS,
    FINISHED,
    SEND_PING_ACK,
    CONTINUATION,
    // unknown frames must be discarded
    UNKNOWN
}

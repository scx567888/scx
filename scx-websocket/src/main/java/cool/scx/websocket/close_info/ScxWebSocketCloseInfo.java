package cool.scx.websocket.close_info;

public interface ScxWebSocketCloseInfo {

    static ScxWebSocketCloseInfo of(int code, String reason) {
        return new ScxWebSocketCloseInfoImpl(code, reason);
    }

    int code();

    String reason();

}

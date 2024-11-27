package cool.scx.http.web_socket;

public sealed interface ScxWebSocketCloseInfo permits ScxWebSocketCloseInfoImpl, WebSocketCloseInfo {

    int code();

    String reason();

}

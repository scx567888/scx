package cool.scx.http.web_socket;

/**
 * ScxWebSocketCloseInfo
 *
 * @author scx567888
 * @version 0.0.1
 */
public sealed interface ScxWebSocketCloseInfo permits ScxWebSocketCloseInfoImpl, WebSocketCloseInfo {

    int code();

    String reason();

}

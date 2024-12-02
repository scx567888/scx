package cool.scx.http.web_socket;

/**
 * ScxWebSocketCloseInfoImpl
 *
 * @author scx567888
 * @version 0.0.1
 */
public record ScxWebSocketCloseInfoImpl(int code, String reason) implements ScxWebSocketCloseInfo {

}

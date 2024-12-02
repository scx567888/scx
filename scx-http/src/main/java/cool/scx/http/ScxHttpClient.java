package cool.scx.http;

import cool.scx.http.web_socket.ScxClientWebSocketBuilder;

/**
 * ScxHttpClient
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxHttpClient {

    ScxHttpClientRequest request();

    ScxClientWebSocketBuilder webSocket();

}

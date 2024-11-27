package cool.scx.http;

import cool.scx.http.web_socket.ScxClientWebSocketBuilder;

/**
 * ScxHttpClient
 */
public interface ScxHttpClient {

    ScxHttpClientRequest request();

    ScxClientWebSocketBuilder webSocket();

}

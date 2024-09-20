package cool.scx.http;

/**
 * ScxHttpClient
 */
public interface ScxHttpClient {

    ScxHttpClientRequest request();

    ScxClientWebSocketBuilder webSocket();

}

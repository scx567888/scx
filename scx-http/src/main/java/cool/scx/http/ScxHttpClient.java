package cool.scx.http;

/**
 * ScxHttpClient
 */
public interface ScxHttpClient {

    ScxHttpClientRequest request();

    ScxClientWebSocket webSocket();

}

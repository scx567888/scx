package cool.scx.http;

/**
 * ScxHttpClient
 */
public interface ScxHttpClient {

    ScxHttpClientRequestBuilder request();

    ScxClientWebSocketBuilder webSocket();

}

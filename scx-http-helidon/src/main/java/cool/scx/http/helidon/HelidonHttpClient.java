package cool.scx.http.helidon;

import cool.scx.http.ScxClientWebSocketBuilder;
import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientOptions;
import io.helidon.webclient.api.WebClient;
import io.helidon.webclient.websocket.WsClient;

/**
 * HelidonHttpClient
 */
public class HelidonHttpClient implements ScxHttpClient {

    private final WebClient webClient;
    private final WsClient wsClient;

    public HelidonHttpClient(ScxHttpClientOptions options) {
        this.webClient = WebClient.builder().build();
        this.wsClient = WsClient.builder().build();
    }

    public HelidonHttpClient() {
        this(new ScxHttpClientOptions());
    }

    @Override
    public HelidonHttpClientRequestBuilder request() {
        return new HelidonHttpClientRequestBuilder(webClient);
    }

    @Override
    public ScxClientWebSocketBuilder webSocket() {
        return new HelidonClientWebSocket(wsClient);
    }

}

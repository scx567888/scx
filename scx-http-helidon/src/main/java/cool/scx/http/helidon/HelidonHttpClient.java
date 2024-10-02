package cool.scx.http.helidon;

import cool.scx.http.ScxClientWebSocketBuilder;
import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientOptions;
import io.helidon.webclient.api.WebClient;

/**
 * HelidonHttpClient
 */
public class HelidonHttpClient implements ScxHttpClient {

    private final WebClient webClient;

    public HelidonHttpClient(ScxHttpClientOptions options) {
        this.webClient = WebClient.builder().build();
    }

    public HelidonHttpClient() {
        this(new ScxHttpClientOptions());
    }

    @Override
    public HelidonHttpClientRequest request() {
        return new HelidonHttpClientRequest(webClient);
    }

    @Override
    public ScxClientWebSocketBuilder webSocket() {
        return new HelidonClientWebSocket();
    }

}

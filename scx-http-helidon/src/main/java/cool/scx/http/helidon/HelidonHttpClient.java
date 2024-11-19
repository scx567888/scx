package cool.scx.http.helidon;

import cool.scx.http.ScxClientWebSocketBuilder;
import cool.scx.http.ScxHttpClient;
import io.helidon.webclient.api.WebClient;

/**
 * HelidonHttpClient
 */
public class HelidonHttpClient implements ScxHttpClient {

    private final WebClient webClient;
    private final HelidonHttpClientOptions options;

    public HelidonHttpClient(HelidonHttpClientOptions options) {
        this.options = options;
        var builder = WebClient.builder();
        if (options.proxy() != null) {
            builder.proxy(options().proxy());
        }
        this.webClient = builder.build();
    }

    public HelidonHttpClient() {
        this(new HelidonHttpClientOptions());
    }

    @Override
    public HelidonHttpClientRequest request() {
        return new HelidonHttpClientRequest(this.webClient, this);
    }

    @Override
    public ScxClientWebSocketBuilder webSocket() {
        return new HelidonClientWebSocket();
    }

    public HelidonHttpClientOptions options() {
        return options;
    }

}

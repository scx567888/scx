package cool.scx.http.helidon;

import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.web_socket.ScxClientWebSocketBuilder;
import io.helidon.webclient.api.WebClient;

/**
 * HelidonHttpClient
 *
 * @author scx567888
 * @version 0.0.1
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
    public ScxHttpClientRequest request() {
        return new HelidonHttpClientRequest(this.webClient, this);
    }

    @Override
    public ScxClientWebSocketBuilder webSocket() {
        return new HelidonClientWebSocketBuilder(this);
    }

    public HelidonHttpClientOptions options() {
        return options;
    }

}

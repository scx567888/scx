package cool.scx.http.helidon;

import cool.scx.http.ScxClientWebSocketBuilder;
import cool.scx.http.ScxHttpClient;
import io.helidon.webclient.api.Proxy;
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
            var p = options.proxy();
            var proxy = Proxy.builder().host(p.host()).port(p.port()).username(p.username());
            if (p.password() != null) {
                proxy.password(p.password());
            }
            builder.proxy(proxy.build());
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

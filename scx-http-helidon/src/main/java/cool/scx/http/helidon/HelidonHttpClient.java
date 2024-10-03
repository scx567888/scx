package cool.scx.http.helidon;

import cool.scx.http.ScxClientWebSocketBuilder;
import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientOptions;
import io.helidon.webclient.api.Proxy;
import io.helidon.webclient.api.WebClient;

/**
 * HelidonHttpClient
 */
public class HelidonHttpClient implements ScxHttpClient {

    private final WebClient webClient;
    private final ScxHttpClientOptions options;

    public HelidonHttpClient(ScxHttpClientOptions options) {
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
        this(new ScxHttpClientOptions());
    }

    @Override
    public HelidonHttpClientRequest request() {
        return new HelidonHttpClientRequest(this.webClient, this);
    }

    @Override
    public ScxClientWebSocketBuilder webSocket() {
        return new HelidonClientWebSocket();
    }

    public ScxHttpClientOptions options() {
        return options;
    }

}

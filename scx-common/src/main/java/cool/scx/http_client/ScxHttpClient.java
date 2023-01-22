package cool.scx.http_client;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static cool.scx.util.ScxExceptionHelper.wrap;

public class ScxHttpClient {

    private final HttpClient client;

    public ScxHttpClient() {
        this(new ScxHttpClientOptions());
    }

    public ScxHttpClient(ScxHttpClientOptions options) {
        this(options.toHttpClientBuilder());
    }

    public ScxHttpClient(HttpClient.Builder builder) {
        this.client = builder.build();
    }

    public ScxHttpClientWebSocket webSocket() {
        throw new UnsupportedOperationException();
    }

    public ScxHttpClientResponse request(ScxHttpClientRequest request) {
        var response = wrap(() -> client.send(request.createHttpRequest(), HttpResponse.BodyHandlers.ofByteArray()));
        return new ScxHttpClientResponse(response);
    }

    public CompletableFuture<ScxHttpClientResponse> requestAsync(ScxHttpClientRequest request) {
        var future = client.sendAsync(request.createHttpRequest(), HttpResponse.BodyHandlers.ofByteArray());
        return future.thenApply(ScxHttpClientResponse::new);
    }

}

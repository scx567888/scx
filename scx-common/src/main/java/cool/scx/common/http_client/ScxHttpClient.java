package cool.scx.common.http_client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;

import static java.net.http.HttpResponse.BodyHandlers.ofInputStream;

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

    public CompletableFuture<WebSocket> webSocket(URI uri, WebSocket.Listener listener) {
        return client.newWebSocketBuilder().buildAsync(uri, listener);
    }

    public ScxHttpClientResponse request(ScxHttpClientRequest request) throws IOException, InterruptedException {
        var response = client.send(request.createHttpRequest(), ofInputStream());
        return new ScxHttpClientResponse(response);
    }

    public CompletableFuture<ScxHttpClientResponse> requestAsync(ScxHttpClientRequest request) {
        var future = client.sendAsync(request.createHttpRequest(), ofInputStream());
        return future.thenApply(ScxHttpClientResponse::new);
    }

}

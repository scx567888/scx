package cool.scx.common.http_client;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

import static cool.scx.common.standard.HttpMethod.*;

public final class ScxHttpClientHelper {

    private final static ScxHttpClient DEFAULT_CLIENT = new ScxHttpClient();

    public static ScxHttpClientResponse request(ScxHttpClientRequest request) throws IOException, InterruptedException {
        return DEFAULT_CLIENT.request(request);
    }

    public static CompletableFuture<ScxHttpClientResponse> requestAsync(ScxHttpClientRequest request) {
        return DEFAULT_CLIENT.requestAsync(request);
    }

    public static ScxHttpClientResponse get(String uri) throws IOException, InterruptedException {
        return request(new ScxHttpClientRequest().method(GET).uri(URI.create(uri)));
    }

    public static CompletableFuture<ScxHttpClientResponse> getAsync(String uri) {
        return requestAsync(new ScxHttpClientRequest().method(GET).uri(URI.create(uri)));
    }

    public static ScxHttpClientResponse post(String uri, ScxHttpClientRequestBody body) throws IOException, InterruptedException {
        return request(new ScxHttpClientRequest().method(POST).body(body).uri(URI.create(uri)));
    }

    public static CompletableFuture<ScxHttpClientResponse> postAsync(String uri, ScxHttpClientRequestBody body) {
        return requestAsync(new ScxHttpClientRequest().method(POST).body(body).uri(URI.create(uri)));
    }

    public static ScxHttpClientResponse put(String uri, ScxHttpClientRequestBody body) throws IOException, InterruptedException {
        return request(new ScxHttpClientRequest().method(PUT).body(body).uri(URI.create(uri)));
    }

    public static CompletableFuture<ScxHttpClientResponse> putAsync(String uri, ScxHttpClientRequestBody body) {
        return requestAsync(new ScxHttpClientRequest().method(PUT).body(body).uri(URI.create(uri)));
    }

    public static ScxHttpClientResponse delete(String uri, ScxHttpClientRequestBody body) throws IOException, InterruptedException {
        return request(new ScxHttpClientRequest().method(DELETE).body(body).uri(URI.create(uri)));
    }

    public static CompletableFuture<ScxHttpClientResponse> deleteAsync(String uri, ScxHttpClientRequestBody body) {
        return requestAsync(new ScxHttpClientRequest().method(DELETE).body(body).uri(URI.create(uri)));
    }

}

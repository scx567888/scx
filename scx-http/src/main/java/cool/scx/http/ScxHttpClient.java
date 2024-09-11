package cool.scx.http;

import cool.scx.http_client.ScxHttpClientRequestBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.WebSocket;

import static cool.scx.http.HttpMethod.*;

public interface ScxHttpClient {

    WebSocket webSocket(URI uri, WebSocket.Listener listener);

    ScxHttpClientResponse request(ScxHttpClientRequest request) throws IOException, InterruptedException;

    default ScxHttpClientResponse get(String uri) throws IOException, InterruptedException {
        return request(new ScxHttpClientRequest().method(GET).uri(URI.create(uri)));
    }

    default ScxHttpClientResponse post(String uri, ScxHttpClientRequestBody body) throws IOException, InterruptedException {
        return request(new ScxHttpClientRequest().method(POST).body(body).uri(URI.create(uri)));
    }

    default ScxHttpClientResponse put(String uri, ScxHttpClientRequestBody body) throws IOException, InterruptedException {
        return request(new ScxHttpClientRequest().method(PUT).body(body).uri(URI.create(uri)));
    }

    default ScxHttpClientResponse delete(String uri, ScxHttpClientRequestBody body) throws IOException, InterruptedException {
        return request(new ScxHttpClientRequest().method(DELETE).body(body).uri(URI.create(uri)));
    }

}

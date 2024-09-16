package cool.scx.http;

import java.io.IOException;
import java.net.URI;

public interface ScxHttpClient {

    ScxHttpClientResponse request(ScxHttpClientRequest request) throws IOException, InterruptedException;

    ScxClientWebSocket webSocket(URI uri);

}

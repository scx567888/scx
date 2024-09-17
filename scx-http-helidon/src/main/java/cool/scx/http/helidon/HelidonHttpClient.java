package cool.scx.http.helidon;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import io.helidon.http.HeaderNames;
import io.helidon.http.Method;
import io.helidon.webclient.api.WebClient;
import io.helidon.webclient.websocket.WsClient;

import java.io.IOException;

//todo 
public class HelidonHttpClient implements ScxHttpClient {

    private final WebClient webClient;
    private final WsClient wsClient;

    public HelidonHttpClient(ScxHttpClientOptions options) {
        this.webClient = WebClient.builder().build();
        this.wsClient = WsClient.builder().build();
    }

    @Override
    public ScxHttpClientResponse request(ScxHttpClientRequest request) throws IOException, InterruptedException {
        var method = Method.create(request.method().value());
        var uri = request.uri().toString();
        var headers = request.headers();
        var r = webClient.method(method);
        r.uri(uri);
        for (var h : headers) {
            r.header(HeaderNames.create(h.getKey().value()), h.getValue());
        }
        if (request.body() != null) {
            r.submit(request.body());
        }
        var res = r.request();
        return new HelidonHttpClientResponse(res);
    }

    @Override
    public ScxClientWebSocket webSocket(ScxURI uri) {
        var clientWebSocket = new HelidonClientWebSocket();
        wsClient.connect(uri.toString(), clientWebSocket);
        return clientWebSocket;
    }

}

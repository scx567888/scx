package cool.scx.http.helidon;

import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import io.helidon.http.HeaderNames;
import io.helidon.http.Method;
import io.helidon.webclient.api.WebClient;
import io.helidon.webclient.websocket.WsClient;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * HelidonHttpClient
 */
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
        //此处使用 CountDownLatch 保证一定是调用了 onOpen 之后才返回 clientWebSocket 对象
        //服务端不需要如此处理 因为服务端 实在 onOpen 方法内调用 后续功能的
        var lock = new CountDownLatch(1);
        var clientWebSocket = new HelidonClientWebSocket(lock);
        wsClient.connect(uri.toString(), clientWebSocket);
        try {
            lock.await();
        } catch (InterruptedException _) {

        }
        return clientWebSocket;
    }

}

package cool.scx.http.usagi;

import cool.scx.common.util.Base64Utils;
import cool.scx.common.util.RandomUtils;
import cool.scx.http.*;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.io.InputStreamDataSupplier;
import cool.scx.io.LinkedDataReader;

import java.util.function.Consumer;

import static cool.scx.http.HttpFieldName.CONNECTION;
import static cool.scx.http.HttpFieldName.UPGRADE;
import static cool.scx.http.HttpHelper.generateSecWebSocketAccept;

public class UsagiClientWebSocketBuilder implements ScxClientWebSocketBuilder {

    private final UsagiHttpClient httpClient;
    private ScxURIWritable uri;
    private ScxHttpHeadersWritable headers;
    private Consumer<ScxClientWebSocket> onConnect;

    public UsagiClientWebSocketBuilder(UsagiHttpClient httpClient) {
        this.httpClient = httpClient;
        this.uri = ScxURI.of();
        this.headers = ScxHttpHeaders.of();
    }

    @Override
    public ScxURIWritable uri() {
        return uri;
    }

    @Override
    public ScxClientWebSocketBuilder uri(ScxURI uri) {
        this.uri = ScxURI.of(uri);
        return this;
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return headers;
    }

    @Override
    public ScxClientWebSocketBuilder headers(ScxHttpHeaders headers) {
        this.headers = ScxHttpHeaders.of(headers);
        return this;
    }

    @Override
    public ScxClientWebSocketBuilder onConnect(Consumer<ScxClientWebSocket> onConnect) {
        this.onConnect = onConnect;
        return this;
    }

    @Override
    public void connect() {
        var key = Base64Utils.encodeToString(RandomUtils.randomBytes(16));
        var newHeader = ScxHttpHeaders.of(headers);
        newHeader.add(CONNECTION, "Upgrade");
        newHeader.add(UPGRADE, "websocket");
        newHeader.add("Sec-Websocket-Key", key);
        newHeader.add("Host", "127.0.0.1");
        newHeader.add("Sec-WebSocket-Version", "13");

        var scheme = uri.scheme();
        if ("ws".equals(scheme)) {
            uri.scheme("http");
        } else if ("wss".equals(scheme)) {
            uri.scheme("https");
        }
        var request = httpClient.request()
                .method(HttpMethod.GET)
                .uri(uri)
                .headers(newHeader);
        //todo 未完成
        var response = request.send();
        if (response.status() != HttpStatusCode.SWITCHING_PROTOCOLS) {
            throw new RuntimeException("Unexpected response status: " + response.status());
        }
        var secWebsocketAccept = response.headers().get("Sec-WebSocket-Accept");

        var expectedSecWebsocketAccept = generateSecWebSocketAccept(key);

        if (!expectedSecWebsocketAccept.equals(secWebsocketAccept)) {
            throw new RuntimeException("Unexpected Sec-WebSocket-Accept: " + expectedSecWebsocketAccept);
        }
        var connect = ((UsagiHttpClientRequest) request).connect;
        var in = connect.inputStream();
        var out = connect.outputStream();

        //todo 此处 UsagiClientWebSocket 没有掩码 操作
        var webSocket = new UsagiClientWebSocket(new LinkedDataReader(new InputStreamDataSupplier(in)), out);
        if (onConnect != null) {
            onConnect.accept(webSocket);
        }
        webSocket.start();

    }

}

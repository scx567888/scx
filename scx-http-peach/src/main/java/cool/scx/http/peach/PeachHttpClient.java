package cool.scx.http.peach;

import cool.scx.http.ScxClientWebSocketBuilder;
import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientRequest;
import cool.scx.net.ScxTCPClient;
import cool.scx.net.ScxTCPClientOptions;

import java.util.function.Function;

public class PeachHttpClient implements ScxHttpClient {

    private final Function<ScxTCPClientOptions, ScxTCPClient> tcpClientBuilder;
    private final ScxTCPClient tcpClient;

    public PeachHttpClient(Function<ScxTCPClientOptions, ScxTCPClient> tcpClientBuilder) {
        this.tcpClientBuilder = tcpClientBuilder;
        this.tcpClient = tcpClientBuilder.apply(null);
    }

    @Override
    public ScxHttpClientRequest request() {
        return new PeachHttpClientRequest();
    }

    @Override
    public ScxClientWebSocketBuilder webSocket() {
        return null;
    }

}

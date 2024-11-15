package cool.scx.http.usagi;

import cool.scx.http.ScxClientWebSocketBuilder;
import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientRequest;
import cool.scx.net.ScxTCPClient;
import cool.scx.net.ScxTCPClientOptions;

import java.util.function.Function;

public class UsagiHttpClient implements ScxHttpClient {

    private final Function<ScxTCPClientOptions, ScxTCPClient> tcpClientBuilder;
    private final ScxTCPClient tcpClient;

    public UsagiHttpClient(Function<ScxTCPClientOptions, ScxTCPClient> tcpClientBuilder) {
        this.tcpClientBuilder = tcpClientBuilder;
        this.tcpClient = tcpClientBuilder.apply(null);
    }

    @Override
    public ScxHttpClientRequest request() {
        return new UsagiHttpClientRequest();
    }

    @Override
    public ScxClientWebSocketBuilder webSocket() {
        return null;
    }

}

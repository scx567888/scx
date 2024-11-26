package cool.scx.http.usagi;

import cool.scx.http.ScxClientWebSocketBuilder;
import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientRequest;
import cool.scx.net.ScxTCPClientOptions;

public class UsagiHttpClient implements ScxHttpClient {

    final ScxTCPClientOptions options;

    public UsagiHttpClient(ScxTCPClientOptions options) {
        this.options = options;
    }

    public UsagiHttpClient() {
        this(new ScxTCPClientOptions());
    }

    @Override
    public ScxHttpClientRequest request() {
        return new UsagiHttpClientRequest(this);
    }

    @Override
    public ScxClientWebSocketBuilder webSocket() {
        return new UsagiClientWebSocketBuilder(this);
    }

}

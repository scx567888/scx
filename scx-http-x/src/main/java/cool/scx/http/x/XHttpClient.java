package cool.scx.http.x;

import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.web_socket.ScxClientWebSocketBuilder;
import cool.scx.http.x.web_socket.XClientWebSocketBuilder;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class XHttpClient implements ScxHttpClient {

    private final XHttpClientOptions options;

    public XHttpClient(XHttpClientOptions options) {
        this.options = options;
    }

    public XHttpClient() {
        this(new XHttpClientOptions());
    }

    public XHttpClientOptions options() {
        return options;
    }

    @Override
    public ScxHttpClientRequest request() {
        return new XHttpClientRequest(this);
    }

    @Override
    public ScxClientWebSocketBuilder webSocket() {
        return new XClientWebSocketBuilder(this);
    }

}

package cool.scx.http.usagi;

import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.web_socket.ScxClientWebSocketBuilder;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpClient implements ScxHttpClient {

    final UsagiHttpClientOptions options;

    public UsagiHttpClient(UsagiHttpClientOptions options) {
        this.options = options;
    }

    public UsagiHttpClient() {
        this(new UsagiHttpClientOptions());
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

package cool.scx.http_server.impl.helidon;

import cool.scx.http_server.ScxHttpRequest;
import cool.scx.http_server.ScxHttpResponse;
import io.helidon.webserver.ConnectionContext;
import io.helidon.webserver.http.RoutingRequest;
import io.helidon.webserver.http.RoutingResponse;

public class HelidonRequest implements ScxHttpRequest {

    private final ConnectionContext ctx;
    private final RoutingRequest request;
    private final ScxHttpResponse response;

    public HelidonRequest(ConnectionContext ctx, RoutingRequest request, RoutingResponse response) {
        this.ctx = ctx;
        this.request = request;
        this.response = new HelidonResponse(response) ;
    }

    @Override
    public ScxHttpResponse response() {
        return this.response;
    }

    @Override
    public byte[] body() {
        return new byte[0];
    }
    
}

package cool.scx.http.helidon;

import io.helidon.webserver.ConnectionContext;
import io.helidon.webserver.http.*;

import java.util.function.Supplier;

/**
 * HelidonHttpRouting
 *
 * @author scx567888
 * @version 0.0.1
 */
class HelidonHttpRouting implements HttpRouting, HttpRouting.Builder {

    private final HelidonHttpServer server;

    public HelidonHttpRouting(HelidonHttpServer server) {
        this.server = server;
    }

    @Override
    public void route(ConnectionContext ctx, RoutingRequest request, RoutingResponse response) {
        if (server.requestHandler != null) {
            server.requestHandler.accept(new HelidonHttpServerRequest(ctx, request, response, server));
        }
    }

    @Override
    public HttpSecurity security() {
        return null;
    }

    @Override
    public Builder register(HttpService... service) {
        return this;
    }

    @Override
    public Builder register(String path, HttpService... service) {
        return this;
    }

    @Override
    public Builder route(HttpRoute route) {
        return this;
    }

    @Override
    public Builder addFilter(Filter filter) {
        return this;
    }

    @Override
    public Builder addFeature(Supplier<? extends HttpFeature> feature) {
        return this;
    }

    @Override
    public <T extends Throwable> Builder error(Class<T> exceptionClass, ErrorHandler<? super T> handler) {
        return this;
    }

    @Override
    public Builder maxReRouteCount(int maxReRouteCount) {
        return this;
    }

    @Override
    public Builder security(HttpSecurity security) {
        return this;
    }

    @Override
    public Builder copy() {
        return this;
    }

    @Override
    public HttpRouting build() {
        return this;
    }

}

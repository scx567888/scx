package cool.scx.http_server.impl.helidon;

import io.helidon.webserver.ConnectionContext;
import io.helidon.webserver.http.*;

import java.util.function.Supplier;

class HelidonHttpRouting implements HttpRouting, HttpRouting.Builder {

    private final HelidonHttpServer server;

    public HelidonHttpRouting(HelidonHttpServer server) {
        this.server = server;
    }

    @Override
    public void route(ConnectionContext ctx, RoutingRequest request, RoutingResponse response) {
        server.requestHandler.accept(new HelidonRequest(ctx, request, response));
    }

    @Override
    public HttpSecurity security() {
        return null;
    }

    @Override
    public Builder register(HttpService... service) {
        return null;
    }

    @Override
    public Builder register(String path, HttpService... service) {
        return null;
    }

    @Override
    public Builder route(HttpRoute route) {
        return null;
    }

    @Override
    public Builder addFilter(Filter filter) {
        return null;
    }

    @Override
    public Builder addFeature(Supplier<? extends HttpFeature> feature) {
        return null;
    }

    @Override
    public <T extends Throwable> Builder error(Class<T> exceptionClass, ErrorHandler<? super T> handler) {
        return null;
    }

    @Override
    public Builder maxReRouteCount(int maxReRouteCount) {
        return null;
    }

    @Override
    public Builder security(HttpSecurity security) {
        return null;
    }

    @Override
    public Builder copy() {
        return null;
    }

    @Override
    public HttpRouting build() {
        return this;
    }

}

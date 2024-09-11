package cool.scx.http;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScxRouter implements Consumer<ScxHttpServerRequest> {

    List<ScxRoute> routes;

    public ScxRouter() {
        this.routes = new ArrayList<>();
    }

    public void addRoute(ScxRoute route) {
        routes.add(route);
    }

    @Override
    public void accept(ScxHttpServerRequest scxHttpRequest) {
        var routingContext = new ScxRoutingContext(scxHttpRequest, routes);
        routingContext.next();
    }

}

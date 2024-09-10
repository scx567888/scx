package cool.scx.http_server;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScxRouter implements Consumer<ScxHttpRequest> {

    List<ScxRoute> routes = new ArrayList<>();

    public void addRoute(ScxRoute route) {
        routes.add(route);
    }

    @Override
    public void accept(ScxHttpRequest scxHttpRequest) {
        var routingContext = new ScxRoutingContext(scxHttpRequest, routes);
        routingContext.next();
    }

}

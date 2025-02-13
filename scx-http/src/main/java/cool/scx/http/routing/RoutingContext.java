package cool.scx.http.routing;

import cool.scx.http.Parameters;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;

/**
 * RoutingContext
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface RoutingContext {

    <T extends ScxHttpServerRequest> T request();

    <T extends ScxHttpServerResponse> T response();

    void next();

    Parameters<String, String> pathParams();

    <T> T get(String name);

    RoutingContext put(String name, Object value);

}

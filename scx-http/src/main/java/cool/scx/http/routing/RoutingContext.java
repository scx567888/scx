package cool.scx.http.routing;

import cool.scx.http.Parameters;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.exception.MethodNotAllowedException;
import cool.scx.http.exception.NotFoundException;
import cool.scx.http.exception.ScxHttpException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static cool.scx.http.HttpStatusCode.INTERNAL_SERVER_ERROR;

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

    RoutingContext set(String name, Object value);

}

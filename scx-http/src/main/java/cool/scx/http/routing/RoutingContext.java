package cool.scx.http.routing;

import cool.scx.http.parameters.Parameters;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;

import java.util.Map;

/// RoutingContext
///
/// @author scx567888
/// @version 0.0.1
@SuppressWarnings("unchecked")
public interface RoutingContext {

    <T extends ScxHttpServerRequest> T request();

    <T extends ScxHttpServerResponse> T response();

    void next();

    Parameters<String, String> pathParams();

    <T> Map<String, T> data();

    //************* 以下为 data 辅助方法 *************

    default RoutingContext put(String key, Object obj) {
        data().put(key, obj);
        return this;
    }

    default <T> T get(String key) {
        return (T) data().get(key);
    }

    default <T> T getOrDefault(String key, T defaultValue) {
        return (T) data().getOrDefault(key, defaultValue);
    }

    default <T> T remove(String key) {
        return (T) data().remove(key);
    }

}

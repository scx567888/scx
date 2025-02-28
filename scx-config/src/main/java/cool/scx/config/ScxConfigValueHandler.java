package cool.scx.config;

import com.fasterxml.jackson.databind.JsonNode;

/// ScxConfigValueHandler
///
/// @author scx567888
/// @version 0.0.1
@FunctionalInterface
public interface ScxConfigValueHandler<T> {

    T handle(String keyPath, JsonNode rawValue);

}

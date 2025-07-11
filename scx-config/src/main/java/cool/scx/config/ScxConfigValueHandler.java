package cool.scx.config;

import cool.scx.object.node.Node;

/// ScxConfigValueHandler
///
/// @author scx567888
/// @version 0.0.1
@FunctionalInterface
public interface ScxConfigValueHandler<T> {

    T handle(String keyPath, Node rawValue);

}

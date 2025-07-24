package cool.scx.config.source;

import cool.scx.object.NodeHelper;
import cool.scx.object.node.ObjectNode;

import java.util.Map;

/// MapConfigSource
///
/// @author scx567888
/// @version 0.0.1
public final class MapConfigSource extends AbstractConfigSource {

    private MapConfigSource(Map<String, Object> map) {
        this.configMapping = loadFromMap(map);
    }

    public static ObjectNode loadFromMap(Map<String, Object> map) {
        var configMapping = new ObjectNode();
        map.forEach((k, v) -> NodeHelper.set(configMapping, k, v));
        return configMapping;
    }

    public static MapConfigSource of(Map<String, Object> configMapping) {
        return new MapConfigSource(configMapping);
    }

}

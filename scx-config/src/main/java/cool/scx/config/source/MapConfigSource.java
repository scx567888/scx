package cool.scx.config.source;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.common.jackson.JsonNodeHelper;

import java.util.Map;

public final class MapConfigSource extends AbstractConfigSource {

    private MapConfigSource(Map<String, Object> map) {
        this.configMapping = loadFromMap(map);
    }

    public static ObjectNode loadFromMap(Map<String, Object> map) {
        var configMapping = JsonNodeFactory.instance.objectNode();
        map.forEach((k, v) -> JsonNodeHelper.set(configMapping, k, v));
        return configMapping;
    }

    public static MapConfigSource of(Map<String, Object> configMapping) {
        return new MapConfigSource(configMapping);
    }

}

package cool.scx.config.source;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.common.util.JsonNodeHelper;
import cool.scx.config.ScxConfigSource;

import java.util.Map;

public final class MapConfigSource implements ScxConfigSource {

    private final ObjectNode configMapping = JsonNodeFactory.instance.objectNode();

    private MapConfigSource(Map<String, Object> map) {
        map.forEach((k, v) -> JsonNodeHelper.set(this.configMapping, k, v));
    }

    public static MapConfigSource of(Map<String, Object> configMapping) {
        return new MapConfigSource(configMapping);
    }

    @Override
    public ObjectNode configMapping() {
        return configMapping;
    }

}

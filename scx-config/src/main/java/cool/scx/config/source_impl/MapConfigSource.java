package cool.scx.config.source_impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.config.ScxConfigSource;
import cool.scx.util.JsonNodeHelper;

import java.util.Map;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class MapConfigSource implements ScxConfigSource {

    private final ObjectNode configMapping = JsonNodeFactory.instance.objectNode();

    /**
     * <p>Constructor for MapConfigSource.</p>
     *
     * @param map a {@link java.util.Map} object
     */
    private MapConfigSource(Map<String, Object> map) {
        map.forEach((k, v) -> JsonNodeHelper.set(this.configMapping, k, v));
    }

    /**
     * a
     *
     * @param configMapping a
     * @return a
     */
    public static MapConfigSource of(Map<String, Object> configMapping) {
        return new MapConfigSource(configMapping);
    }

    @Override
    public ObjectNode configMapping() {
        return configMapping;
    }

}

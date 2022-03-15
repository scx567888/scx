package cool.scx.config.impl;

import cool.scx.config.ScxConfigSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * a
 */
public final class MapConfigSource implements ScxConfigSource {

    private final LinkedHashMap<String, Object> configMapping = new LinkedHashMap<>();

    private MapConfigSource(Map<String, Object> configMapping) {
        this.configMapping.putAll(configMapping);
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
    public Map<String, Object> getConfigMapping() {
        return configMapping;
    }

}
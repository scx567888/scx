package cool.scx.config.impl;

import cool.scx.config.ScxConfigSource;

import java.util.HashMap;
import java.util.Map;

public final class MapConfigSource implements ScxConfigSource {

    private final Map<String, Object> configMapping = new HashMap<>();

    private MapConfigSource(Map<String, Object> configMapping) {
        this.configMapping.putAll(configMapping);
    }

    public static MapConfigSource of(Map<String, Object> configMapping) {
        return new MapConfigSource(configMapping);
    }

    @Override
    public Map<String, Object> getConfigMapping() {
        return configMapping;
    }

}
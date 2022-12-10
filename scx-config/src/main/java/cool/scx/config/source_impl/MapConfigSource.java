package cool.scx.config.source_impl;

import cool.scx.config.ScxConfigSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class MapConfigSource implements ScxConfigSource {

    private final LinkedHashMap<String, Object> configMapping = new LinkedHashMap<>();

    /**
     * <p>Constructor for MapConfigSource.</p>
     *
     * @param configMapping a {@link java.util.Map} object
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getConfigMapping() {
        return configMapping;
    }

}

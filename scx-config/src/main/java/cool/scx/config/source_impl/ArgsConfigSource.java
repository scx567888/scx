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
public final class ArgsConfigSource implements ScxConfigSource {

    private final Map<String, Object> configMapping = new LinkedHashMap<>();

    /**
     * a
     *
     * @param args a
     */
    private ArgsConfigSource(String... args) {
        for (var arg : args) {
            if (arg.startsWith("--")) {
                var strings = arg.substring(2).split("=");
                if (strings.length == 2) {
                    configMapping.put(strings[0], strings[1]);
                }
            }
        }
    }

    /**
     * a
     *
     * @param args a
     * @return a
     */
    public static ArgsConfigSource of(String... args) {
        return new ArgsConfigSource(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getConfigMapping() {
        return configMapping;
    }

}

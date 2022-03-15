package cool.scx.config.impl;

import cool.scx.config.ScxConfigSource;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ArgsConfigSource implements ScxConfigSource {

    private final Map<String, Object> configMapping = new LinkedHashMap<>();

    public ArgsConfigSource(String... args) {
        for (var arg : args) {
            if (arg.startsWith("--")) {
                var strings = arg.substring(2).split("=");
                if (strings.length == 2) {
                    configMapping.put(strings[0], strings[1]);
                }
            }
        }
    }

    public static ArgsConfigSource of(String... args) {
        return new ArgsConfigSource(args);
    }

    @Override
    public Map<String, Object> getConfigMapping() {
        return configMapping;
    }

}

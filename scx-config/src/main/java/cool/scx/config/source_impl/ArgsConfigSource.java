package cool.scx.config.source_impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.config.ScxConfigSource;
import cool.scx.util.JsonNodeHelper;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ArgsConfigSource implements ScxConfigSource {

    private final ObjectNode configMapping = JsonNodeFactory.instance.objectNode();

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
                    JsonNodeHelper.set(configMapping, strings[0], strings[1]);
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

    @Override
    public ObjectNode configMapping() {
        return configMapping;
    }

}

package cool.scx.config.source;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.common.jackson.JsonNodeHelper;

/// ArgsConfigSource
///
/// @author scx567888
/// @version 0.0.1
public final class ArgsConfigSource extends AbstractConfigSource {

    private ArgsConfigSource(String... args) {
        this.configMapping = loadFromArgs(args);
    }

    public static ObjectNode loadFromArgs(String... args) {
        var configMapping = JsonNodeFactory.instance.objectNode();
        for (var arg : args) {
            if (arg.startsWith("--")) {
                var strings = arg.substring(2).split("=");
                if (strings.length == 2) {
                    JsonNodeHelper.set(configMapping, strings[0], strings[1]);
                }
            }
        }
        return configMapping;
    }

    public static ArgsConfigSource of(String... args) {
        return new ArgsConfigSource(args);
    }

}

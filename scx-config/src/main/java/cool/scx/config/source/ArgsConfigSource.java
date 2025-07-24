package cool.scx.config.source;

import cool.scx.object.NodeHelper;
import cool.scx.object.node.ObjectNode;

/// ArgsConfigSource
///
/// @author scx567888
/// @version 0.0.1
public final class ArgsConfigSource extends AbstractConfigSource {

    private ArgsConfigSource(String... args) {
        this.configMapping = loadFromArgs(args);
    }

    public static ObjectNode loadFromArgs(String... args) {
        var configMapping = new ObjectNode();
        for (var arg : args) {
            if (arg.startsWith("--")) {
                var strings = arg.substring(2).split("=");
                if (strings.length == 2) {
                    NodeHelper.set(configMapping, strings[0], strings[1]);
                }
            }
        }
        return configMapping;
    }

    public static ArgsConfigSource of(String... args) {
        return new ArgsConfigSource(args);
    }

}

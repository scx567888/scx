package cool.scx.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class ScxConfigDefaultValue {
    public static Map<String, Object> getDefaultConfig() {
        var map = new HashMap<String, Object>();
        map.put("scx.port", 8080);
        map.put("scx.tombstone", false);
        map.put("scx.allowed-origin", "*");
        map.put("scx.template.root", "AppRoot:/c/");
        map.put("scx.static-servers", new Object[0]);
        map.put("scx.https.enabled", false);
        map.put("scx.https.ssl-path", "");
        map.put("scx.https.ssl-password", "");
        map.put("scx.data-source.host", "127.0.0.1");
        map.put("scx.data-source.port", 3306);
        map.put("scx.data-source.database", "");
        map.put("scx.data-source.username", "");
        map.put("scx.data-source.password", "");
        map.put("scx.data-source.parameters", new HashSet<>());
        map.put("scx.log.root.level", "ERROR");
        map.put("scx.log.root.logging-type", "CONSOLE");
        map.put("scx.log.root.stored-directory", "AppRoot:logs");
        return map;
    }
}

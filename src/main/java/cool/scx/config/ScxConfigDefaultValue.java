package cool.scx.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxConfigDefaultValue {

    /**
     * 默认配置键值对, 以便在没有配置文件的时候可以使项目正确启动
     */
    private static final Map<String, Object> DEFAULT_CONFIG_MAP;

    static {
        DEFAULT_CONFIG_MAP = new HashMap<>();
        DEFAULT_CONFIG_MAP.put("scx.port", 8080);
        DEFAULT_CONFIG_MAP.put("scx.tombstone", false);
        DEFAULT_CONFIG_MAP.put("scx.allowed-origin", "*");
        DEFAULT_CONFIG_MAP.put("scx.template.root", "AppRoot:/c/");
        DEFAULT_CONFIG_MAP.put("scx.static-servers", new Object[0]);
        DEFAULT_CONFIG_MAP.put("scx.https.enabled", false);
        DEFAULT_CONFIG_MAP.put("scx.https.ssl-path", "");
        DEFAULT_CONFIG_MAP.put("scx.https.ssl-password", "");
        DEFAULT_CONFIG_MAP.put("scx.data-source.host", "127.0.0.1");
        DEFAULT_CONFIG_MAP.put("scx.data-source.port", 3306);
        DEFAULT_CONFIG_MAP.put("scx.data-source.database", "");
        DEFAULT_CONFIG_MAP.put("scx.data-source.username", "");
        DEFAULT_CONFIG_MAP.put("scx.data-source.password", "");
        DEFAULT_CONFIG_MAP.put("scx.data-source.parameters", new HashSet<>());
        DEFAULT_CONFIG_MAP.put("scx.logging.default.level", "ERROR");
        DEFAULT_CONFIG_MAP.put("scx.logging.default.type", "CONSOLE");
        DEFAULT_CONFIG_MAP.put("scx.logging.default.stored-directory", "AppRoot:logs");
    }


    /**
     * 返回默认的配置文件
     *
     * @return r
     */
    public static Map<String, Object> defaultConfig() {
        return DEFAULT_CONFIG_MAP;
    }

}

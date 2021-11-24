package cool.scx.util;

import io.vertx.core.MultiMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * map 工具类
 *
 * @author scx567888
 * @version 1.1.2
 */
public final class MapUtils {

    /**
     * 将嵌套的 map 扁平化
     *
     * @param sourceMap 源 map
     * @param parentKey a {@link java.lang.String} object.
     * @return 扁平化后的 map
     */
    public static Map<String, Object> flatMap(Map<?, ?> sourceMap, String parentKey) {
        var result = new LinkedHashMap<String, Object>();
        var prefix = StringUtils.isBlank(parentKey) ? "" : parentKey + ".";
        sourceMap.forEach((key, value) -> {
            var newKey = prefix + key;
            if (value instanceof Map m) {
                result.putAll(flatMap(m, newKey));
            } else {
                result.put(newKey, value);
            }
        });
        return result;
    }

    /**
     * <p>multiMapToMap.</p>
     *
     * @param multiMap a {@link io.vertx.core.MultiMap} object
     * @return a {@link java.util.Map} object
     */
    public static Map<String, Object> multiMapToMap(MultiMap multiMap) {
        var map = new HashMap<String, Object>();
        for (var m : multiMap) {
            map.put(m.getKey(), m.getValue());
        }
        return map;
    }

}

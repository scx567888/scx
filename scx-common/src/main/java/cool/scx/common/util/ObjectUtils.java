package cool.scx.common.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/// 处理对象的简易工具类
///
/// @author scx567888
/// @version 0.0.1
public final class ObjectUtils {

    /// 将嵌套的 map 扁平化
    ///
    /// @param sourceMap 源 map
    /// @param parentKey a [java.lang.String] object.
    /// @return 扁平化后的 map
    private static Map<String, Object> flatMap0(Map<?, ?> sourceMap, String parentKey) {
        var result = new LinkedHashMap<String, Object>();
        var prefix = StringUtils.isBlank(parentKey) ? "" : parentKey + ".";
        sourceMap.forEach((key, value) -> {
            var newKey = prefix + key;
            if (value instanceof Map<?, ?> m) {
                result.putAll(flatMap0(m, newKey));
            } else {
                result.put(newKey, value);
            }
        });
        return result;
    }

    /// 将嵌套的 map 扁平化
    ///
    /// @param sourceMap 源 map
    /// @return 扁平化后的 map
    public static Map<String, Object> flatMap(Map<?, ?> sourceMap) {
        return flatMap0(sourceMap, null);
    }

    /// null -> true
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Optional<?> optional) {
            return optional.isEmpty();
        } else if (obj instanceof CharSequence charSequence) {
            return charSequence.isEmpty();
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection<?> collection) {
            return collection.isEmpty();
        } else if (obj instanceof Map<?, ?> map) {
            return map.isEmpty();
        } else {
            return false;
        }
    }

    /// null -> true
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

}

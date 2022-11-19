package cool.scx.core.mvc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.util.ObjectUtils;
import io.vertx.core.MultiMap;

import java.io.IOException;
import java.util.Map;

import static cool.scx.util.ObjectUtils.jsonMapper;

public class ScxMappingHelper {

    public static Object getFromMap(String name, MultiMap map, boolean useAll, JavaType javaType) {
        if (useAll) {
            return map;
        } else if (javaType.isCollectionLikeType() || javaType.isArrayType()) {
            return map.getAll(name);
        } else {
            return map.get(name);
        }
    }

    public static Object getFromMap(String name, Map<String, String> map, boolean useAll) {
        return useAll ? map : map.get(name);
    }

    /**
     * a
     *
     * @param jsonNode a
     * @param type     a
     * @param <T>      a
     * @return a
     * @throws java.io.IOException a
     */
    public static <T> T readValue(JsonNode jsonNode, JavaType type) throws IOException {
        return jsonMapper(ObjectUtils.Option.IGNORE_JSON_IGNORE).readerFor(type).readValue(jsonNode);
    }

    public static JsonNode getFromJsonNode(String name, JsonNode jsonNode, boolean useAll) {
        var tempValue = jsonNode;
        if (!useAll) {
            var split = name.split("\\.");
            for (var s : split) {
                if (tempValue != null) {
                    tempValue = tempValue.get(s);
                } else {
                    break;
                }
            }
        }
        return jsonNode;
    }

}

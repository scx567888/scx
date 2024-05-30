package cool.scx.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * 用来处理一些简单的 JsonNode 的节点操作
 * todo 后续需要将其拓展出完整的 JsonPath
 */
public final class JsonNodeHelper {

    public static JsonNode get(JsonNode jsonNode, String jsonPath) {
        var paths = jsonPath.split("\\.");
        var result = jsonNode;
        for (var path : paths) {
            if (result != null) {
                result = result.get(path);
            } else {
                break;
            }
        }
        return result;
    }

    public static JsonNode set(ObjectNode objectNode, String jsonPath, Object object) {
        var paths = jsonPath.split("\\.");
        var jsonNode = objectNode;
        for (int i = 0; i < paths.length; i = i + 1) {
            var path = paths[i];
            //最后一个
            if (i == paths.length - 1) {
                put(jsonNode, path, object);
            } else {
                var n = jsonNode.get(path);
                jsonNode = switch (n) {
                    case null -> jsonNode.putObject(path);
                    case ObjectNode nn -> nn;
                    default -> throw new RuntimeException("路径中已有数据且不为 Object, 无法 set");
                };
            }
        }
        return objectNode;
    }

    private static void put(ObjectNode objectNode, String fieldName, Object object) {
        objectNode.set(fieldName, ObjectUtils.convertValue(object, JsonNode.class));
    }

    public static void merge(ObjectNode valueToUpdate, ObjectNode value) {
        try {
            ObjectUtils.jsonMapper().readerForUpdating(valueToUpdate).readValue(value);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}

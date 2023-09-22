package cool.scx.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 简易版 JsonPath
 */
public class JsonNodeHelper {

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
        for (int i = 0; i < paths.length; i++) {
            var path = paths[i];
            //最后一个
            if (i == paths.length - 1) {
                put(jsonNode, path, object);
            } else {
                jsonNode = jsonNode.putObject(path);
            }
        }
        return objectNode;
    }

    private static void put(ObjectNode objectNode, String fieldName, Object object) {
        objectNode.set(fieldName, ObjectUtils.convertValue(object, JsonNode.class));
    }

}

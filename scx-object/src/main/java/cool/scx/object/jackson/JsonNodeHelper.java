package cool.scx.object.jackson;

import cool.scx.object.ScxObject;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;

/// 用来处理一些简单的 JsonNode 的节点操作
/// todo 后续需要将其拓展出完整的 JsonPath
///
/// @author scx567888
/// @version 0.0.1
public final class JsonNodeHelper {

    public static Node get(Node jsonNode, String jsonPath) {
        var paths = jsonPath.split("\\.");
        var result = jsonNode;
        for (var path : paths) {
            if (result != null) {
                if (result instanceof ObjectNode i) {
                    result = i.get(path);
                } else {
                    result = null;
                }
            } else {
                break;
            }
        }
        return result;
    }

    public static Node set(ObjectNode objectNode, String jsonPath, Object object) {
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
                    case null -> {
                        var entries = new ObjectNode();
                        jsonNode.put(path, entries);
                        yield entries;
                    }
                    case ObjectNode nn -> nn;
                    default -> throw new RuntimeException("路径中已有数据且不为 Object, 无法 set");
                };
            }
        }
        return objectNode;
    }

    private static void put(ObjectNode objectNode, String fieldName, Object object) {
        objectNode.put(fieldName, ScxObject.convertValue(object, Node.class));
    }

    public static void merge(ObjectNode valueToUpdate, ObjectNode value) {
        try {
            ScxObject.merge(valueToUpdate, value);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

}

package cool.scx.data.query.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.query.GroupBy;

import java.util.ArrayList;

public class GroupByDeserializer {

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            return switch (type) {
                case "GroupBy" -> deserializeGroupBy(v);
                default -> v;
            };
        } else if (v.isTextual()) {
            return deserializeString(v);
        } else if (v.isArray()) {
            return deserializeAll(v);
        }
        return null;
    }

    private GroupBy deserializeGroupBy(JsonNode v) {
        var name = v.path("name").asText();
        return new GroupBy(name);
    }

    private String deserializeString(JsonNode v) {
        return v.textValue();
    }

    private Object[] deserializeAll(JsonNode v) {
        var s = new ArrayList<>();
        for (var jsonNode : v) {
            s.add(deserialize(jsonNode));
        }
        return s.toArray();
    }

}

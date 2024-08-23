package cool.scx.data.query.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.query.GroupBy;
import cool.scx.data.query.GroupByBody;
import cool.scx.data.query.GroupByOption;

import java.util.ArrayList;

public class GroupByDeserializer {

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            return switch (type) {
                case "GroupBy" -> deserializeGroupBy(v);
                case "GroupByBody" -> deserializeGroupByBody(v);
                default -> null;
            };
        } else if (v.isTextual()) {
            return deserializeString(v);
        } else if (v.isArray()) {
            return deserializeAll(v);
        }
        return null;
    }

    public GroupBy deserializeGroupBy(JsonNode v) {
        if (v == null) {
            return new GroupBy();
        }
        var groupBy = new GroupBy();
        groupBy.set(deserializeAll(v.get("clauses")));
        return groupBy;
    }

    public GroupByBody deserializeGroupByBody(JsonNode v) {
        var name = v.path("name").asText();
        return new GroupByBody(name, new GroupByOption.Info());
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

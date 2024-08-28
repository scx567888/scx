package cool.scx.data.query.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.query.*;

import java.util.ArrayList;

public class OrderByDeserializer {

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            return switch (type) {
                case "OrderBySet" -> deserializeOrderBySet(v);
                case "OrderBy" -> deserializeOrderBy(v);
                default -> v;
            };
        } else if (v.isTextual()) {
            return deserializeString(v);
        } else if (v.isArray()) {
            return deserializeAll(v);
        }
        return null;
    }

    public OrderBySet deserializeOrderBySet(JsonNode v) {
        var orderByBodySet = new OrderBySet();
        var clauses = deserializeAll(v.get("clauses"));
        for (var clause : clauses) {
            if (clause instanceof OrderBy b) {
                orderByBodySet.add(b.name(), b.orderByType());
            }
        }
        return orderByBodySet;
    }

    public OrderBy deserializeOrderBy(JsonNode v) {
        var name = v.path("name").asText();
        var orderByType = OrderByType.of(v.path("orderByType").asText());
        return new OrderBy(name, orderByType, new OrderByOption.Info());
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

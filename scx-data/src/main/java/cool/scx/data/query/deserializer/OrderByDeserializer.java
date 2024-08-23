package cool.scx.data.query.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.query.*;

import java.util.ArrayList;

public class OrderByDeserializer {

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            return switch (type) {
                case "OrderBy" -> deserializeOrderBy(v);
                case "OrderByBodySet" -> deserializeOrderByBodySet(v);
                case "OrderByBody" -> deserializeOrderByBody(v);
                default -> null;
            };
        } else if (v.isTextual()) {
            return deserializeString(v);
        } else if (v.isArray()) {
            return deserializeAll(v);
        }
        return null;
    }

    public OrderBy deserializeOrderBy(JsonNode v) {
        if (v == null) {
            return new OrderBy();
        }
        var orderBy = new OrderBy();
        orderBy.set(deserializeAll(v.get("clauses")));
        return orderBy;
    }

    public OrderByBodySet deserializeOrderByBodySet(JsonNode v) {
        var orderByBodySet = new OrderByBodySet();
        var clauses = deserializeAll(v.get("clauses"));
        for (var clause : clauses) {
            if (clause instanceof OrderByBody b) {
                orderByBodySet.add(b.name(), b.orderByType());
            }
        }
        return orderByBodySet;
    }

    public OrderByBody deserializeOrderByBody(JsonNode v) {
        var name = v.path("name").asText();
        var orderByType = OrderByType.of(v.path("orderByType").asText());
        return new OrderByBody(name, orderByType, new OrderByOption.Info());
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

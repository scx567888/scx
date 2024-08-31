package cool.scx.data.query.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.OrderByType;
import cool.scx.data.query.QueryOption;

import java.util.ArrayList;

import static cool.scx.common.util.ObjectUtils.convertValue;
import static cool.scx.data.query.QueryOption.*;

public class OrderByDeserializer {

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            return switch (type) {
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

    private OrderBy deserializeOrderBy(JsonNode v) {
        var name = v.get("name").asText();
        var orderByType = convertValue(v.get("orderByType"), OrderByType.class);
        var info = convertValue(v.path("info"), Info.class);
        return new OrderBy(name, orderByType,info);
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

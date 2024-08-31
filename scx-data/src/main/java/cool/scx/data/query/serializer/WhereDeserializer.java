package cool.scx.data.query.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.query.*;
import cool.scx.data.query.QueryOption.Info;

import java.util.ArrayList;

import static cool.scx.common.util.ObjectUtils.convertValue;

public class WhereDeserializer {

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            return switch (type) {
                case "Logic" -> deserializeLogic(v);
                case "WhereClause" -> deserializeWhereClause(v);
                case "Where" -> deserializeWhere(v);
                default -> v;
            };
        } else if (v.isTextual()) {
            return deserializeString(v);
        } else if (v.isArray()) {
            return deserializeAll(v);
        }
        return null;
    }

    private Logic deserializeLogic(JsonNode v) {
        var logicType = convertValue(v.get("logicType"), LogicType.class);
        var clauses = deserializeAll(v.get("clauses"));
        return new Logic(logicType).add(clauses);
    }

    private WhereClause deserializeWhereClause(JsonNode v) {
        var whereClause = v.get("whereClause").asText();
        var params = convertValue(v.get("params"), Object[].class);
        return new WhereClause(whereClause, params);
    }

    private Where deserializeWhere(JsonNode v) {
        var name = v.get("name").asText();
        var whereType = convertValue(v.get("whereType"), WhereType.class);
        var value1 = convertValue(v.get("value1"), Object.class);
        var value2 = convertValue(v.get("value2"), Object.class);
        var info = convertValue(v.get("info"), Info.class);
        return new Where(name, whereType, value1, value2,info);
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

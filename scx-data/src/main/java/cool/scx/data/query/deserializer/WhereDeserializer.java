package cool.scx.data.query.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.query.*;

import java.util.ArrayList;

public class WhereDeserializer {

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            return switch (type) {
                case "Where" -> deserializeWhere(v);
                case "Logic" -> deserializeLogic(v);
                case "WhereClause" -> deserializeWhereClause(v);
                case "WhereBody" -> deserializeWhereBody(v);
                default -> null;
            };
        } else if (v.isTextual()) {
            return deserializeString(v);
        } else if (v.isArray()) {
            return deserializeAll(v);
        }
        return null;
    }

    public Where deserializeWhere(JsonNode v) {
        if (v == null) {
            return new Where();
        }
        var where = new Where();
        where.set(deserializeAll(v.get("clauses")));
        return where;
    }

    private Logic deserializeLogic(JsonNode v) {
        var logicType = v.get("logicType").asText();
        if (logicType.equals("OR")) {
            return new OR(deserializeAll(v.get("clauses")));
        } else if (logicType.equals("AND")) {
            return new AND(deserializeAll(v.get("clauses")));
        } else {
            return null;
        }
    }

    private WhereClause deserializeWhereClause(JsonNode v) {
        var whereClause = v.get("whereClause").asText();
        var params = ObjectUtils.convertValue(v.get("params"), Object[].class);
        return new WhereClause(whereClause, params);
    }

    private WhereBody deserializeWhereBody(JsonNode v) {
        var name = v.get("name").asText();
        var whereType = WhereType.of(v.get("whereType").asText());
        var value1 = ObjectUtils.convertValue(v.get("value1"), Object.class);
        var value2 = ObjectUtils.convertValue(v.get("value2"), Object.class);
        return new WhereBody(name, whereType, value1, value2);
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

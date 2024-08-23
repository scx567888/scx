package cool.scx.data.query.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.Query;
import cool.scx.data.query.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class WhereSerializer {

    //****************** 序列化 *********************

    public Object serialize(Object obj) {
        return switch (obj) {
            case Where w -> serializeWhere(w);
            case Logic l -> serializeLogic(l);
            case WhereClause w -> serializeWhereClause(w);
            case WhereBody whereBody -> serializeWhereBody(whereBody);
            case String str -> serializeString(str);
            case Query q -> serializeWhere(q.getWhere());
            case Object[] q -> serializeAll(q);
            case null, default -> obj;
        };
    }

    public Map<String, Object> serializeWhere(Where where) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Where");
        m.put("clauses", serialize(where.clauses()));
        return m;
    }

    public Map<String, Object> serializeLogic(Logic l) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Logic");
        m.put("logicType", l.logicType());
        m.put("clauses", serializeAll(l.clauses()));
        return m;
    }

    public LinkedHashMap<String, Object> serializeWhereClause(WhereClause w) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "WhereClause");
        m.put("whereClause", w.whereClause());
        m.put("params", w.params());
        return m;
    }

    private Map<String, Object> serializeWhereBody(WhereBody w) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "WhereBody");
        m.put("name", w.name());
        m.put("whereType", w.whereType());
        m.put("value1", w.value1());
        m.put("value2", w.value2());
        return m;
    }

    public String serializeString(String str) {
        return str;
    }

    public final Object[] serializeAll(Object[] objs) {
        var arr = new Object[objs.length];
        for (int i = 0; i < objs.length; i = i + 1) {
            arr[i] = serialize(objs[i]);
        }
        return arr;
    }

    //**************** 反序列化 *********************

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

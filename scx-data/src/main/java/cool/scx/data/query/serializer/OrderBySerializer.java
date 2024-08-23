package cool.scx.data.query.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.Query;
import cool.scx.data.query.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class OrderBySerializer {

    //****************** 序列化 *********************   

    public Object serialize(Object obj) {
        return switch (obj) {
            case OrderBy s -> serializeOrderBy(s);
            case OrderByBodySet s -> serializeOrderByBodySet(s);
            case OrderByBody o -> serializeOrderByBody(o);
            case String s -> serializeString(s);
            case Query q -> serializeOrderBy(q.getOrderBy());
            case Object[] q -> serializeAll(q);
            case null, default -> null;
        };
    }

    public Object serializeOrderBy(OrderBy orderBy) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "OrderBy");
        m.put("clauses", serializeAll(orderBy.clauses()));
        return m;
    }

    public Object serializeOrderByBodySet(OrderByBodySet orderByBodySet) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "OrderBySet");
        m.put("clauses", serializeAll(orderByBodySet.clauses()));
        return m;
    }

    public Object serializeOrderByBody(OrderByBody orderByBody) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "OrderByBody");
        m.put("name", orderByBody.name());
        m.put("orderByType", orderByBody.orderByType());
        m.put("info", orderByBody.info());
        return m;
    }

    public Object serializeString(String s) {
        return s;
    }

    public Object[] serializeAll(Object[] objs) {
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
        var orderBy = new OrderBy();
        orderBy.set(deserialize(v.get("clauses")));
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

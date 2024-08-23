package cool.scx.data.query.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.Query;
import cool.scx.data.query.GroupBy;
import cool.scx.data.query.GroupByBody;
import cool.scx.data.query.GroupByOption;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GroupBySerializer {


    //****************** 序列化 *********************   

    public Object serialize(Object obj) {
        return switch (obj) {
            case GroupBy s -> serializeGroupBy(s);
            case GroupByBody s -> serializeGroupByBody(s);
            case String s -> serializeString(s);
            case Query q -> serializeGroupBy(q.getGroupBy());
            case Object[] q -> serializeAll(q);
            case null, default -> null;
        };
    }

    public Object serializeGroupBy(GroupBy groupBy) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "GroupBy");
        m.put("clauses", serializeAll(groupBy.clauses()));
        return m;
    }

    public Object serializeGroupByBody(GroupByBody groupByBody) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "GroupByBody");
        m.put("name", groupByBody.name());
        m.put("info", groupByBody.info());
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
        var groupBy = new GroupBy();
        groupBy.add(deserialize(v.get("clauses")));
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

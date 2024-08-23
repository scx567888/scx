package cool.scx.data.query.serializer;

import cool.scx.data.Query;
import cool.scx.data.query.GroupBy;
import cool.scx.data.query.GroupByBody;

import java.util.LinkedHashMap;

public class GroupBySerializer {

    public Object serialize(Object obj) {
        return switch (obj) {
            case GroupBy s -> serializeGroupBy(s);
            case GroupByBody s -> serializeGroupByBody(s);
            case String s -> serializeString(s);
            case Object[] q -> serializeAll(q);
            case Query q -> serializeGroupBy(q.getGroupBy());
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

}

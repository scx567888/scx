package cool.scx.data.query.serializer;

import cool.scx.data.query.GroupBy;
import cool.scx.data.query.Query;

import java.util.LinkedHashMap;

/// GroupBySerializer
///
/// @author scx567888
/// @version 0.0.1
public class GroupBySerializer {

    public Object serialize(Object obj) {
        return switch (obj) {
            case String s -> serializeString(s);
            case GroupBy g -> serializeGroupBy(g);
            case Query q -> serializeQuery(q);
            case Object[] o -> serializeAll(o);
            default -> obj;
        };
    }

    private Object serializeString(String s) {
        return s;
    }

    private Object serializeGroupBy(GroupBy g) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "GroupBy");
        m.put("name", g.name());
        m.put("info", g.info());
        return m;
    }

    private Object[] serializeQuery(Query q) {
//        return serializeAll(q.getGroupBy()); todo
        return null;
    }

    private Object[] serializeAll(Object[] objs) {
        var arr = new Object[objs.length];
        for (int i = 0; i < objs.length; i = i + 1) {
            arr[i] = serialize(objs[i]);
        }
        return arr;
    }

}

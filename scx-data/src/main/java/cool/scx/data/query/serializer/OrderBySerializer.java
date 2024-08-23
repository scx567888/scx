package cool.scx.data.query.serializer;

import cool.scx.data.Query;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.OrderByBody;
import cool.scx.data.query.OrderByBodySet;

import java.util.LinkedHashMap;

public class OrderBySerializer {

    public Object serialize(Object obj) {
        return switch (obj) {
            case OrderBy s -> serializeOrderBy(s);
            case OrderByBodySet s -> serializeOrderByBodySet(s);
            case OrderByBody o -> serializeOrderByBody(o);
            case String s -> serializeString(s);
            case Object[] q -> serializeAll(q);
            case Query q -> serializeOrderBy(q.getOrderBy());
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

}

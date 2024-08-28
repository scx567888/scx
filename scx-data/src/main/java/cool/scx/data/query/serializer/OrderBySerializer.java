package cool.scx.data.query.serializer;

import cool.scx.data.Query;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.OrderBySet;

import java.util.LinkedHashMap;

public class OrderBySerializer {

    public Object serialize(Object obj) {
        return switch (obj) {
            case String s -> serializeString(s);
            case OrderBy o -> serializeOrderBy(o);
            case OrderBySet s -> serializeOrderBySet(s);
            case Query q -> serializeQuery(q);
            default -> null;
        };
    }

    public Object serializeString(String s) {
        return s;
    }

    public Object serializeOrderBy(OrderBy orderByBody) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "OrderByBody");
        m.put("name", orderByBody.name());
        m.put("orderByType", orderByBody.orderByType());
        m.put("info", orderByBody.info());
        return m;
    }

    public Object serializeOrderBySet(OrderBySet orderByBodySet) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "OrderBySet");
        m.put("clauses", serializeAll(orderByBodySet.clauses()));
        return m;
    }

    public Object serializeQuery(Query q) {
        return serializeAll(q.getOrderBy());
    }

    public Object[] serializeAll(Object[] objs) {
        var arr = new Object[objs.length];
        for (int i = 0; i < objs.length; i = i + 1) {
            arr[i] = serialize(objs[i]);
        }
        return arr;
    }

}

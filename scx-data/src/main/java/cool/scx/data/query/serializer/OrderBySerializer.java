package cool.scx.data.query.serializer;

import cool.scx.data.Query;
import cool.scx.data.query.OrderBy;

import java.util.LinkedHashMap;

public class OrderBySerializer {

    public Object serialize(Object obj) {
        return switch (obj) {
            case String s -> serializeString(s);
            case OrderBy o -> serializeOrderBy(o);
            case Query q -> serializeQuery(q);
            case Object[] o -> serializeAll(o);
            default -> obj;
        };
    }

    private Object serializeString(String s) {
        return s;
    }

    private Object serializeOrderBy(OrderBy orderByBody) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "OrderBy");
        m.put("name", orderByBody.name());
        m.put("orderByType", orderByBody.orderByType());
        m.put("info", orderByBody.info());
        return m;
    }

    private Object serializeQuery(Query q) {
        return serializeAll(q.getOrderBy());
    }

    private Object[] serializeAll(Object[] objs) {
        var arr = new Object[objs.length];
        for (int i = 0; i < objs.length; i = i + 1) {
            arr[i] = serialize(objs[i]);
        }
        return arr;
    }

}

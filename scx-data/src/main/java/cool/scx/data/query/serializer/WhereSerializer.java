package cool.scx.data.query.serializer;

import cool.scx.data.query.Logic;
import cool.scx.data.query.Query;
import cool.scx.data.query.Where;
import cool.scx.data.query.WhereClause;

import java.util.LinkedHashMap;
import java.util.Map;

/// WhereSerializer
///
/// @author scx567888
/// @version 0.0.1
public class WhereSerializer {

    public Object serialize(Object obj) {
        return switch (obj) {
            case String str -> serializeString(str);
            case WhereClause w -> serializeWhereClause(w);
            case Logic l -> serializeLogic(l);
            case Where whereBody -> serializeWhere(whereBody);
            case Query q -> serializeQuery(q);
            case Object[] o -> serializeAll(o);
            default -> obj;
        };
    }

    private String serializeString(String str) {
        return str;
    }

    private LinkedHashMap<String, Object> serializeWhereClause(WhereClause w) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "WhereClause");
        m.put("whereClause", w.whereClause());
        m.put("params", w.params());
        return m;
    }

    private Map<String, Object> serializeLogic(Logic l) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Logic");
        m.put("logicType", l.logicType());
        m.put("clauses", serializeAll(l.clauses()));
        return m;
    }

    private Map<String, Object> serializeWhere(Where w) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Where");
        m.put("name", w.name());
        m.put("whereType", w.whereType());
        m.put("value1", w.value1());
        m.put("value2", w.value2());
        m.put("info", w.info());
        return m;
    }

    private Object[] serializeQuery(Query q) {
        return serializeAll(q.getWhere());
    }

    private Object[] serializeAll(Object[] objs) {
        var arr = new Object[objs.length];
        for (int i = 0; i < objs.length; i = i + 1) {
            arr[i] = serialize(objs[i]);
        }
        return arr;
    }

}

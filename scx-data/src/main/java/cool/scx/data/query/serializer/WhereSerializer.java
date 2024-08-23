package cool.scx.data.query.serializer;

import cool.scx.data.Query;
import cool.scx.data.query.Logic;
import cool.scx.data.query.Where;
import cool.scx.data.query.WhereBody;
import cool.scx.data.query.WhereClause;

import java.util.LinkedHashMap;
import java.util.Map;

public class WhereSerializer {

    public Object serialize(Object obj) {
        return switch (obj) {
            case Where w -> serializeWhere(w);
            case Logic l -> serializeLogic(l);
            case WhereClause w -> serializeWhereClause(w);
            case WhereBody whereBody -> serializeWhereBody(whereBody);
            case String str -> serializeString(str);
            case Object[] q -> serializeAll(q);
            case Query q -> serializeWhere(q.getWhere());
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

}

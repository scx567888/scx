package cool.scx.data.query.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.query.*;

import java.util.LinkedHashMap;
import java.util.Map;

/// QuerySerializer
///
/// @author scx567888
/// @version 0.0.1
public class QuerySerializer {

    public static final QuerySerializer QUERY_SERIALIZER = new QuerySerializer();

    public String toJson(Query query) throws JsonProcessingException {
        var v = serialize(query);
        return ObjectUtils.jsonMapper().writeValueAsString(v);
    }

    public Object serialize(Query query) {
        return serializeQuery(query);
    }

    private Map<String, Object> serializeQuery(Query query) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Query");
        m.put("where", serializeWhere(query.getWhere()));
        m.put("orderBys", serializeOrderBys(query.getOrderBys()));
        m.put("offset", query.getOffset());
        m.put("limit", query.getLimit());
        return m;
    }

    public Object serializeWhere(Where obj) {
        return switch (obj) {
            case WhereClause w -> serializeWhereClause(w);
            case And a -> serializeAnd(a);
            case Or o -> serializeOr(o);
            case Not n -> serializeNot(n);
            case Condition conditionBody -> serializeCondition(conditionBody);
            case Query q -> serializeWhere(q.getWhere());
            default -> throw new IllegalArgumentException("Unknown Where type: " + obj);
        };
    }

    private LinkedHashMap<String, Object> serializeWhereClause(WhereClause w) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "WhereClause");
        m.put("whereClause", w.whereClause());
        m.put("params", w.params());
        return m;
    }

    private Map<String, Object> serializeAnd(And a) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "And");
        m.put("clauses", serializeWhereAll(a.clauses()));
        return m;
    }

    private Map<String, Object> serializeOr(Or o) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Or");
        m.put("clauses", serializeWhereAll(o.clauses()));
        return m;
    }

    private Map<String, Object> serializeNot(Not n) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Not");
        m.put("clause", serializeWhere(n.clause()));
        return m;
    }

    private Map<String, Object> serializeCondition(Condition w) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Condition");
        m.put("selector", w.selector());
        m.put("conditionType", w.conditionType());
        m.put("value1", w.value1());
        m.put("value2", w.value2());
        m.put("useExpression", w.useExpression());
        m.put("useExpressionValue", w.useExpressionValue());
        m.put("skipIfInfo", serializeSkipIfInfo(w.skipIfInfo()));
        return m;
    }

    private Object[] serializeWhereAll(Where[] objs) {
        var arr = new Object[objs.length];
        for (int i = 0; i < objs.length; i = i + 1) {
            arr[i] = serializeWhere(objs[i]);
        }
        return arr;
    }

    public Object serializeOrderBys(OrderBy... objs) {
        var arr = new Object[objs.length];
        for (int i = 0; i < objs.length; i = i + 1) {
            arr[i] = serializeOrderBy(objs[i]);
        }
        return arr;
    }

    private Object serializeOrderBy(OrderBy orderByBody) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "OrderBy");
        m.put("selector", orderByBody.selector());
        m.put("orderByType", orderByBody.orderByType());
        m.put("useExpression", orderByBody.useExpression());
        return m;
    }

    public LinkedHashMap<String, Object> serializeSkipIfInfo(SkipIfInfo info) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "SkipIfInfo");
        m.put("skipIfNull", info.skipIfNull());
        m.put("skipIfEmptyList", info.skipIfEmptyList());
        m.put("skipIfEmptyString", info.skipIfEmptyString());
        m.put("skipIfBlankString", info.skipIfBlankString());
        return m;
    }

}

package cool.scx.data.query.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.Query;

import java.util.LinkedHashMap;

public class QuerySerializer {

    private final WhereSerializer whereSerializer;
    private final GroupBySerializer groupBySerializer;
    private final OrderBySerializer orderBySerializer;
    private final LimitInfoSerializer limitInfoSerializer;

    public QuerySerializer() {
        this.whereSerializer = new WhereSerializer();
        this.groupBySerializer = new GroupBySerializer();
        this.orderBySerializer = new OrderBySerializer();
        this.limitInfoSerializer = new LimitInfoSerializer();
    }

    public String toJson(Query query) throws JsonProcessingException {
        var v = serializeAny(query);
        return ObjectUtils.jsonMapper().writeValueAsString(v);
    }

    public Object serializeAny(Object object) {
        Object serialize = serialize(object);
        if (serialize == null) {
            serialize = whereSerializer.serialize(object);
        }
        if (serialize == null) {
            serialize = groupBySerializer.serialize(object);
        }
        if (serialize == null) {
            serialize = orderBySerializer.serialize(object);
        }
        if (serialize == null) {
            serialize = limitInfoSerializer.serialize(object);
        }
        return serialize;
    }

    public Object serialize(Object obj) {
        return switch (obj) {
            case Query s -> serializeQueryImpl(s);
            case null, default -> null;
        };
    }

    public LinkedHashMap<String, Object> serializeQueryImpl(Query query) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Query");
        m.put("where", whereSerializer.serialize(query.getWhere()));
        m.put("groupBy", groupBySerializer.serialize(query.getGroupBy()));
        m.put("orderBy", orderBySerializer.serialize(query.getOrderBy()));
        m.put("limitInfo", limitInfoSerializer.serialize(query.getLimitInfo()));
        return m;
    }

}

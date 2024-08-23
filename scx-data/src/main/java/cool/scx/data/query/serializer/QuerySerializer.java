package cool.scx.data.query.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.Query;
import cool.scx.data.query.QueryImpl;

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
        var v = serialize(query);
        return ObjectUtils.jsonMapper().writeValueAsString(v);
    }

    public Query fromJson(String json) throws JsonProcessingException {
        var v = ObjectUtils.jsonMapper().readTree(json);
        return deserialize(v);
    }

    public LinkedHashMap<String, Object> serialize(Query query) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Query");
        m.put("where", whereSerializer.serialize(query.getWhere()));
        m.put("groupBy", groupBySerializer.serialize(query.getGroupBy()));
        m.put("orderBy", orderBySerializer.serialize(query.getOrderBy()));
        m.put("limitInfo", limitInfoSerializer.serialize(query.getLimitInfo()));
        return m;
    }

    public Query deserialize(JsonNode objectNode) {
        var where = whereSerializer.deserialize(objectNode.get("where"));
        var groupBy = groupBySerializer.deserialize(objectNode.get("groupBy"));
        var orderBy = orderBySerializer.deserialize(objectNode.get("orderBy"));
        var limitInfo = limitInfoSerializer.deserialize(objectNode.get("limitInfo"));
        return new QueryImpl(where, groupBy, orderBy, limitInfo);
    }

}

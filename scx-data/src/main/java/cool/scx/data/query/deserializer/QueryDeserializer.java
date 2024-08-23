package cool.scx.data.query.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.Query;
import cool.scx.data.query.QueryImpl;

public class QueryDeserializer {

    public static final QueryDeserializer QUERY_DESERIALIZER = new QueryDeserializer();

    private final WhereDeserializer whereDeserializer;
    private final GroupByDeserializer groupByDeserializer;
    private final OrderByDeserializer orderByDeserializer;
    private final LimitInfoDeserializer limitInfoDeserializer;

    public QueryDeserializer() {
        this.whereDeserializer = new WhereDeserializer();
        this.groupByDeserializer = new GroupByDeserializer();
        this.orderByDeserializer = new OrderByDeserializer();
        this.limitInfoDeserializer = new LimitInfoDeserializer();
    }

    public Query fromJson(String json) throws JsonProcessingException {
        var v = ObjectUtils.jsonMapper().readTree(json);
        return deserializeAny(v);
    }

    public Query deserializeAny(JsonNode v) {
        Object deserialize = deserialize(v);
        if (deserialize == null) {
            deserialize = whereDeserializer.deserialize(v);
        }
        if (deserialize == null) {
            deserialize = groupByDeserializer.deserialize(v);
        }
        if (deserialize == null) {
            deserialize = orderByDeserializer.deserialize(v);
        }
        if (deserialize == null) {
            deserialize = limitInfoDeserializer.deserialize(v);
        }
        return (Query) deserialize;
    }

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("Query")) {
                return deserializeQuery(v);
            }
        }
        return null;
    }

    public Query deserializeQuery(JsonNode objectNode) {
        if (objectNode == null) {
            return new QueryImpl();
        }
        var where = whereDeserializer.deserializeWhere(objectNode.get("where"));
        var groupBy = groupByDeserializer.deserializeGroupBy(objectNode.get("groupBy"));
        var orderBy = orderByDeserializer.deserializeOrderBy(objectNode.get("orderBy"));
        var limitInfo = limitInfoDeserializer.deserializeLimitInfo(objectNode.get("limitInfo"));
        return new QueryImpl(where, groupBy, orderBy, limitInfo);
    }

}

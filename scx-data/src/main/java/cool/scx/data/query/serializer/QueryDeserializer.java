package cool.scx.data.query.serializer;

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

    public QueryDeserializer() {
        this.whereDeserializer = new WhereDeserializer();
        this.groupByDeserializer = new GroupByDeserializer();
        this.orderByDeserializer = new OrderByDeserializer();
    }

    public Query fromJson(String json) throws JsonProcessingException {
        var v = ObjectUtils.jsonMapper().readTree(json);
        return deserializeQuery(v);
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
        var where = whereDeserializer.deserialize(objectNode.get("where"));
        var groupBy = groupByDeserializer.deserialize(objectNode.get("groupBy"));
        var orderBy = orderByDeserializer.deserialize(objectNode.get("orderBy"));
        var offset = objectNode.get("offset").asLong();
        var limit = objectNode.get("limit").asLong();
        return new QueryImpl().where(where).groupBy(groupBy).orderBy(orderBy).offset(offset).limit(limit);
    }

}

package cool.scx.data.query.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.query.Query;
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
        var query = new QueryImpl();
        if (objectNode == null) {
            return query;
        }
        if (objectNode.get("where") != null && !objectNode.get("where").isNull()) {
            var where = whereDeserializer.deserialize(objectNode.get("where"));
            query.where(where);
        }
        if (objectNode.get("groupBy") != null && !objectNode.get("groupBy").isNull()) {
            var groupBy = groupByDeserializer.deserialize(objectNode.path("groupBy"));
            query.groupBy(groupBy);
        }
        if (objectNode.get("orderBy") != null && !objectNode.get("orderBy").isNull()) {
            var orderBy = orderByDeserializer.deserialize(objectNode.path("orderBy"));
            query.orderBy(orderBy);
        }
        if (objectNode.get("offset") != null && !objectNode.get("offset").isNull()) {
            query.offset(objectNode.get("offset").asLong());
        }
        if (objectNode.get("limit") != null && !objectNode.get("limit").isNull()) {
            query.limit(objectNode.get("limit").asLong());
        }
        return query;
    }

}

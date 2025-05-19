package cool.scx.data.query.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.query.*;

import java.util.ArrayList;

import static cool.scx.common.util.ObjectUtils.convertValue;

/// QueryDeserializer
///
/// @author scx567888
/// @version 0.0.1
public class QueryDeserializer {

    public static final QueryDeserializer QUERY_DESERIALIZER = new QueryDeserializer();

    public Query fromJson(String json) throws JsonProcessingException {
        var v = ObjectUtils.jsonMapper().readTree(json);
        return deserializeQuery(v);
    }

    public Query deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("Query")) {
                return deserializeQuery(v);
            }
        }
        throw new IllegalArgumentException("Unknown query type: " + v);
    }

    public Query deserializeQuery(JsonNode objectNode) {
        var query = new QueryImpl();
        if (objectNode == null) {
            return query;
        }

        var whereNode = objectNode.get("where");
        var orderByNode = objectNode.get("orderBy");
        var offsetNode = objectNode.get("offset");
        var limitNode = objectNode.get("limit");
        if (whereNode != null && !whereNode.isNull()) {
            var where = deserializeWhere(whereNode);
            query.where(where);
        }
        if (orderByNode != null && !orderByNode.isNull()) {
            var orderBy = deserializeOrderBy(orderByNode);
            query.orderBy(orderBy);
        }
        if (offsetNode != null && !offsetNode.isNull()) {
            query.offset(offsetNode.asLong());
        }
        if (limitNode != null && !limitNode.isNull()) {
            query.limit(limitNode.asLong());
        }
        return query;
    }

    public Object deserializeOrderBy(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            return switch (type) {
                case "OrderBy" -> deserializeOrderBy0(v);
                default -> v;
            };
        } else if (v.isTextual()) {
            return deserializeString(v);
        } else if (v.isArray()) {
            return deserializeOrderByAll(v);
        }
        return null;
    }

    private OrderBy deserializeOrderBy0(JsonNode v) {
        var name = v.get("name").asText();
        var orderByType = convertValue(v.get("orderByType"), OrderByType.class);
        var info = convertValue(v.path("info"), QueryOption.Info.class);
        return new OrderBy(name, orderByType, info);
    }

    private String deserializeString(JsonNode v) {
        return v.textValue();
    }

    private Object[] deserializeOrderByAll(JsonNode v) {
        var s = new ArrayList<>();
        for (var jsonNode : v) {
            s.add(deserializeOrderBy(jsonNode));
        }
        return s.toArray();
    }

    public Object deserializeWhere(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            return switch (type) {
                case "And" -> deserializeAnd(v);
                case "Or" -> deserializeOr(v);
                case "Not" -> deserializeNot(v);
                case "WhereClause" -> deserializeWhereClause(v);
                case "Where" -> deserializeWhere0(v);
                default -> v;
            };
        } else if (v.isTextual()) {
            return deserializeString(v);
        } else if (v.isArray()) {
            return deserializeWhereAll(v);
        }
        return null;
    }

    private Junction deserializeAnd(JsonNode v) {
        var clauses = deserializeWhereAll(v.get("clauses"));
        return new And().add(clauses);
    }

    private Junction deserializeOr(JsonNode v) {
        var clauses = deserializeWhereAll(v.get("clauses"));
        return new Or().add(clauses);
    }

    private Not deserializeNot(JsonNode v) {
        var clause = deserializeWhere(v.get("clause"));
        return new Not(clause);
    }

    private WhereClause deserializeWhereClause(JsonNode v) {
        var whereClause = v.get("whereClause").asText();
        var params = convertValue(v.get("params"), Object[].class);
        return new WhereClause(whereClause, params);
    }

    private Where deserializeWhere0(JsonNode v) {
        var name = v.get("name").asText();
        var whereType = convertValue(v.get("whereType"), WhereType.class);
        var value1 = convertValue(v.get("value1"), Object.class);
        var value2 = convertValue(v.get("value2"), Object.class);
        var info = convertValue(v.get("info"), QueryOption.Info.class);
        return new Where(name, whereType, value1, value2, info);
    }

    private Object[] deserializeWhereAll(JsonNode v) {
        var s = new ArrayList<>();
        for (var jsonNode : v) {
            s.add(deserializeWhere(jsonNode));
        }
        return s.toArray();
    }

}

package cool.scx.data.query.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.build_control.BuildControlInfo;
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
        return deserialize(v);
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
        var orderBysNode = objectNode.get("orderBys");
        var offsetNode = objectNode.get("offset");
        var limitNode = objectNode.get("limit");
        if (whereNode != null && !whereNode.isNull()) {
            var where = deserializeWhere(whereNode);
            query.where(where);
        }
        if (orderBysNode != null && !orderBysNode.isNull()) {
            var orderBys = deserializeOrderByAll(orderBysNode);
            query.orderBys(orderBys);
        }
        if (offsetNode != null && !offsetNode.isNull()) {
            query.offset(offsetNode.asLong());
        }
        if (limitNode != null && !limitNode.isNull()) {
            query.limit(limitNode.asLong());
        }
        return query;
    }

    public OrderBy deserializeOrderBy(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("OrderBy")) {
                return deserializeOrderBy0(v);
            }
        }
        throw new IllegalArgumentException("Unknown orderBy type: " + v);
    }

    private OrderBy deserializeOrderBy0(JsonNode v) {
        var selector = v.get("selector").asText();
        var orderByType = convertValue(v.get("orderByType"), OrderByType.class);
        var info = convertValue(v.path("info"), BuildControlInfo.class);
        return new OrderBy(selector, orderByType, info);
    }

    private String deserializeString(JsonNode v) {
        return v.textValue();
    }

    private OrderBy[] deserializeOrderByAll(JsonNode v) {
        var s = new ArrayList<OrderBy>();
        for (var jsonNode : v) {
            s.add(deserializeOrderBy(jsonNode));
        }
        return s.toArray(OrderBy[]::new);
    }

    public Object deserializeWhere(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            return switch (type) {
                case "And" -> deserializeAnd(v);
                case "Or" -> deserializeOr(v);
                case "Not" -> deserializeNot(v);
                case "WhereClause" -> deserializeWhereClause(v);
                case "Condition" -> deserializeCondition(v);
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

    private Condition deserializeCondition(JsonNode v) {
        var selector = v.get("selector").asText();
        var conditionType = convertValue(v.get("conditionType"), ConditionType.class);
        var value1 = convertValue(v.get("value1"), Object.class);
        var value2 = convertValue(v.get("value2"), Object.class);
        var info = convertValue(v.get("info"), BuildControlInfo.class);
        return new Condition(selector, conditionType, value1, value2, info);
    }

    private Object[] deserializeWhereAll(JsonNode v) {
        var s = new ArrayList<>();
        for (var jsonNode : v) {
            s.add(deserializeWhere(jsonNode));
        }
        return s.toArray();
    }

}

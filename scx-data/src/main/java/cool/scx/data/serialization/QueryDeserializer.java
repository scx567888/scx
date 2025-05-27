package cool.scx.data.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.query.*;

import java.util.ArrayList;
import java.util.List;

public class QueryDeserializer {

    public static Query deserializeQueryFromJson(String json) throws DeserializationException {
        try {
            var v = ObjectUtils.jsonMapper().readTree(json);
            return deserializeQuery(v);
        } catch (JsonProcessingException e) {
            throw new DeserializationException(e);
        }
    }

    public static Query deserializeQuery(JsonNode v) throws DeserializationException {
        if (v == null || v.isNull()) {
            throw new DeserializationException("Query node is null");
        }
        if (!v.isObject()) {
            throw new DeserializationException("Query node is not an object: " + v);
        }
        var typeNode = v.get("@type");
        if (typeNode == null || !"Query".equals(typeNode.asText())) {
            throw new DeserializationException("Unknown or missing @type for Query: " + v);
        }

        var query = new QueryImpl();

        Where where = deserializeWhere(v.get("where"));

        query.where(where);

        OrderBy[] orderBys = deserializeOrderBys(v.get("orderBys"));

        query.orderBys(orderBys);

        if (v.has("offset") && !v.get("offset").isNull()) {
            var offset = v.get("offset").asInt();
            query.offset(offset);
        }

        if (v.has("limit") && !v.get("limit").isNull()) {
            var limit = v.get("limit").asInt();
            query.limit(limit);
        }

        return query;
    }

    private static Where deserializeWhere(JsonNode node) throws DeserializationException {
        if (node == null || node.isNull()) {
            return null;
        }
        if (!node.isObject()) {
            throw new DeserializationException("Where node is not an object: " + node);
        }
        var typeNode = node.get("@type");
        if (typeNode == null) {
            throw new DeserializationException("Missing @type in Where node: " + node);
        }
        String type = typeNode.asText();
        return switch (type) {
            case "WhereClause" -> deserializeWhereClause(node);
            case "And" -> deserializeAnd(node);
            case "Or" -> deserializeOr(node);
            case "Not" -> deserializeNot(node);
            case "Condition" -> deserializeCondition(node);
            default -> throw new DeserializationException("Unknown Where type: " + type);
        };
    }

    private static WhereClause deserializeWhereClause(JsonNode node) {
        String expression = node.has("expression") ? node.get("expression").asText() : null;
        JsonNode paramsNode = node.get("params");
        Object[] params = ObjectUtils.jsonMapper().convertValue(paramsNode, Object[].class);
        return new WhereClause(expression, params);
    }

    private static Junction deserializeAnd(JsonNode node) throws DeserializationException {
        JsonNode clausesNode = node.get("clauses");
        Where[] clauses = deserializeWhereArray(clausesNode);
        return new And().add(clauses);
    }

    private static Junction deserializeOr(JsonNode node) throws DeserializationException {
        JsonNode clausesNode = node.get("clauses");
        Where[] clauses = deserializeWhereArray(clausesNode);
        return new Or().add(clauses);
    }

    private static Not deserializeNot(JsonNode node) throws DeserializationException {
        JsonNode clauseNode = node.get("clause");
        Where clause = deserializeWhere(clauseNode);
        return new Not(clause);
    }

    private static Condition deserializeCondition(JsonNode node) {
        String selector = node.has("selector") ? node.get("selector").asText() : null;
        ConditionType conditionType = node.has("conditionType")
                ? ConditionType.valueOf(node.get("conditionType").asText())
                : null;
        JsonNode value1Node = node.get("value1");
        Object value1 = ObjectUtils.jsonMapper().convertValue(value1Node, Object.class);
        JsonNode value2Node = node.get("value2");
        Object value2 = ObjectUtils.jsonMapper().convertValue(value2Node, Object.class);
        boolean useExpression = node.has("useExpression") && node.get("useExpression").asBoolean(false);
        boolean useExpressionValue = node.has("useExpressionValue") && node.get("useExpressionValue").asBoolean(false);
        SkipIfInfo skipIfInfo = node.has("skipIfInfo") ? deserializeSkipIfInfo(node.get("skipIfInfo")) : null;

        return new Condition(selector, conditionType, value1, value2, useExpression, useExpressionValue, skipIfInfo);
    }

    private static Where[] deserializeWhereArray(JsonNode arrayNode) throws DeserializationException {
        if (arrayNode == null || !arrayNode.isArray()) {
            return new Where[0];
        }
        List<Where> list = new ArrayList<>();
        for (JsonNode node : arrayNode) {
            list.add(deserializeWhere(node));
        }
        return list.toArray(new Where[0]);
    }

    public static OrderBy[] deserializeOrderBys(JsonNode arrayNode) {
        if (arrayNode == null || !arrayNode.isArray()) {
            return new OrderBy[0];
        }
        List<OrderBy> list = new ArrayList<>();
        for (JsonNode node : arrayNode) {
            list.add(deserializeOrderBy(node));
        }
        return list.toArray(new OrderBy[0]);
    }

    private static OrderBy deserializeOrderBy(JsonNode node) {
        String selector = node.has("selector") ? node.get("selector").asText() : null;
        OrderByType orderByType = node.has("orderByType")
                ? OrderByType.valueOf(node.get("orderByType").asText())
                : null;
        boolean useExpression = node.has("useExpression") && node.get("useExpression").asBoolean(false);
        return new OrderBy(selector, orderByType, useExpression);
    }

    private static SkipIfInfo deserializeSkipIfInfo(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        boolean skipIfNull = node.has("skipIfNull") && node.get("skipIfNull").asBoolean(false);
        boolean skipIfEmptyList = node.has("skipIfEmptyList") && node.get("skipIfEmptyList").asBoolean(false);
        boolean skipIfEmptyString = node.has("skipIfEmptyString") && node.get("skipIfEmptyString").asBoolean(false);
        boolean skipIfBlankString = node.has("skipIfBlankString") && node.get("skipIfBlankString").asBoolean(false);
        return new SkipIfInfo(skipIfNull, skipIfEmptyList, skipIfEmptyString, skipIfBlankString);
    }

}

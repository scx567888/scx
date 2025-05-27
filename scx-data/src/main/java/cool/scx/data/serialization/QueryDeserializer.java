package cool.scx.data.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.query.*;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.common.util.ObjectUtils.convertValue;
import static cool.scx.common.util.ObjectUtils.jsonMapper;

public class QueryDeserializer {

    public static Query deserializeQueryFromJson(String json) throws DeserializationException {
        try {
            var v = jsonMapper().readTree(json);
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
        Object[] params = convertValue(paramsNode, Object[].class);
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

    private static Condition deserializeCondition(JsonNode node) throws DeserializationException {
        //selector 不允许为空
        var selectorNode = node.get("selector");
        if (selectorNode == null || !selectorNode.isTextual()) {
            throw new DeserializationException("Missing selector in Condition node: " + node);
        }
        var selector = selectorNode.asText();

        //conditionType 也不允许为空
        var conditionTypeNode = node.get("conditionType");
        if (conditionTypeNode == null || !conditionTypeNode.isTextual()) {
            throw new DeserializationException("Missing conditionType in Condition node: " + node);
        }
        var conditionType = convertValue(conditionTypeNode, ConditionType.class);

        // value1 和 value2 都允许 null
        var value1Node = node.get("value1");
        var value2Node = node.get("value2");
        Object value1;
        Object value2;
        if (value1Node == null || value1Node.isNull()) {
            value1 = null;
        } else {
            value1 = convertValue(value1Node, Object.class);
        }
        if (value2Node == null || value2Node.isNull()) {
            value2 = null;
        } else {
            value2 = convertValue(value2Node, Object.class);
        }

        //以下三个都允许 null, 但必须存在默认值
        var useExpressionNode = node.get("useExpression");
        var useExpressionValueNode = node.get("useExpressionValue");
        var skipIfInfoNode = node.get("skipIfInfo");

        boolean useExpression = false;
        boolean useExpressionValue = false;
        SkipIfInfo skipIfInfo = new SkipIfInfo(false, false, false, false);
        if (useExpressionNode != null && !useExpressionNode.isNull()) {
            useExpression = useExpressionNode.asBoolean(false);
        }
        if (useExpressionValueNode != null && !useExpressionValueNode.isNull()) {
            useExpressionValue = useExpressionValueNode.asBoolean(false);
        }
        if (skipIfInfoNode != null && !skipIfInfoNode.isNull()) {
            skipIfInfo = deserializeSkipIfInfo(skipIfInfoNode);
        }
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
        return list.toArray(Where[]::new);
    }

    public static OrderBy[] deserializeOrderBys(JsonNode arrayNode) throws DeserializationException {
        if (arrayNode == null || !arrayNode.isArray()) {
            return new OrderBy[0];
        }
        List<OrderBy> list = new ArrayList<>();
        for (JsonNode node : arrayNode) {
            list.add(deserializeOrderBy(node));
        }
        return list.toArray(OrderBy[]::new);
    }

    private static OrderBy deserializeOrderBy(JsonNode node) throws DeserializationException {
        //selector 不允许为空
        var selectorNode = node.get("selector");
        if (selectorNode == null || !selectorNode.isTextual()) {
            throw new DeserializationException("Missing selector in OrderBy node: " + node);
        }
        var selector = selectorNode.asText();

        //orderByTypeNode 也不允许为空
        var orderByTypeNode = node.get("orderByType");
        if (orderByTypeNode == null || !orderByTypeNode.isTextual()) {
            throw new DeserializationException("Missing orderByType in OrderBy node: " + node);
        }
        var orderByType = convertValue(orderByTypeNode, OrderByType.class);

        //可以为空但是必须有默认值
        var useExpressionNode = node.get("useExpression");
        var useExpression = false;
        if (useExpressionNode != null && !useExpressionNode.isNull()) {
            useExpression = useExpressionNode.asBoolean(false);
        }
        return new OrderBy(selector, orderByType, useExpression);
    }

    private static SkipIfInfo deserializeSkipIfInfo(JsonNode node) {
        if (node == null || node.isNull()) {
            return new SkipIfInfo(false, false, false, false);
        }
        boolean skipIfNull = node.has("skipIfNull") && node.get("skipIfNull").asBoolean(false);
        boolean skipIfEmptyList = node.has("skipIfEmptyList") && node.get("skipIfEmptyList").asBoolean(false);
        boolean skipIfEmptyString = node.has("skipIfEmptyString") && node.get("skipIfEmptyString").asBoolean(false);
        boolean skipIfBlankString = node.has("skipIfBlankString") && node.get("skipIfBlankString").asBoolean(false);
        return new SkipIfInfo(skipIfNull, skipIfEmptyList, skipIfEmptyString, skipIfBlankString);
    }

}

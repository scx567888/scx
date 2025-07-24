package cool.scx.data.serialization;

import cool.scx.data.query.*;
import cool.scx.object.ScxObject;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;
import cool.scx.object.node.ValueNode;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.object.ScxObject.convertValue;

public class QueryDeserializer {

    public static Query deserializeQueryFromJson(String json) throws DeserializationException {
        try {
            var v = ScxObject.fromJson(json);
            return deserializeQuery(v);
        } catch (Exception e) {
            throw new DeserializationException(e);
        }
    }

    public static Query deserializeQuery(Node v) throws DeserializationException {
        if (v == null || v.isNull() || !(v instanceof ObjectNode objNode)) {
            throw new DeserializationException("Query node is null or not an object");
        }

        var typeNode = objNode.get("@type");
        if (!(typeNode instanceof ValueNode vn) || !"Query".equals(vn.asText())) {
            throw new DeserializationException("Unknown or missing @type for Query");
        }

        var query = new QueryImpl();

        query.where(deserializeWhere(objNode.get("where")));
        query.orderBys(deserializeOrderBys(objNode.get("orderBys")));

        var offsetNode = objNode.get("offset");
        if (offsetNode instanceof ValueNode offVN) {
            query.offset(offVN.asInt());
        }

        var limitNode = objNode.get("limit");
        if (limitNode instanceof ValueNode limVN) {
            query.limit(limVN.asInt());
        }

        return query;
    }

    private static Where deserializeWhere(Node node) throws DeserializationException {
        if (node == null || node.isNull()) {
            return null;
        }
        if (!(node instanceof ObjectNode objNode)) {
            throw new DeserializationException("Where node is not an object: " + node);
        }

        var typeNode = objNode.get("@type");
        if (!(typeNode instanceof ValueNode typeVN)) {
            throw new DeserializationException("Missing @type in Where node");
        }

        return switch (typeVN.asText()) {
            case "WhereClause" -> deserializeWhereClause(objNode);
            case "And" -> deserializeAnd(objNode);
            case "Or" -> deserializeOr(objNode);
            case "Not" -> deserializeNot(objNode);
            case "Condition" -> deserializeCondition(objNode);
            default -> throw new DeserializationException("Unknown Where type: " + typeVN.asText());
        };
    }

    private static WhereClause deserializeWhereClause(ObjectNode node) {
        var exprNode = node.get("expression");
        var paramsNode = node.get("params");
        String expression = exprNode instanceof ValueNode vn ? vn.asText() : null;
        Object[] params = convertValue(paramsNode, Object[].class);
        return new WhereClause(expression, params);
    }

    private static Junction deserializeAnd(ObjectNode node) throws DeserializationException {
        var clausesNode = node.get("clauses");
        return new And().add(deserializeWhereArray(clausesNode));
    }

    private static Junction deserializeOr(ObjectNode node) throws DeserializationException {
        var clausesNode = node.get("clauses");
        return new Or().add(deserializeWhereArray(clausesNode));
    }

    private static Not deserializeNot(ObjectNode node) throws DeserializationException {
        var clauseNode = node.get("clause");
        return new Not(deserializeWhere(clauseNode));
    }

    private static Condition deserializeCondition(ObjectNode node) throws DeserializationException {
        var selectorNode = node.get("selector");
        if (!(selectorNode instanceof ValueNode selectorVN)) {
            throw new DeserializationException("Missing selector in Condition");
        }

        var conditionTypeNode = node.get("conditionType");
        if (!(conditionTypeNode instanceof Node)) {
            throw new DeserializationException("Missing conditionType in Condition");
        }
        var conditionType = convertValue(conditionTypeNode, ConditionType.class);

        Object value1 = extractValue(node.get("value1"));
        Object value2 = extractValue(node.get("value2"));

        boolean useExpression = getBooleanOrDefault(node.get("useExpression"), false);
        boolean useExpressionValue = getBooleanOrDefault(node.get("useExpressionValue"), false);
        SkipIfInfo skipIfInfo = node.get("skipIfInfo") != null ? deserializeSkipIfInfo(node.get("skipIfInfo")) : new SkipIfInfo(false, false, false, false);

        return new Condition(selectorVN.asText(), conditionType, value1, value2, useExpression, useExpressionValue, skipIfInfo);
    }

    private static Object extractValue(Node node) {
        if (node == null || node.isNull()) {
            return null;
        }
        return convertValue(node, Object.class);
    }

    private static boolean getBooleanOrDefault(Node node, boolean defaultValue) {
        if (node instanceof ValueNode vn) {
            return vn.asBoolean();
        }
        return defaultValue;
    }

    private static Where[] deserializeWhereArray(Node arrayNode) throws DeserializationException {
        if (!(arrayNode instanceof ArrayNode array)) {
            return new Where[0];
        }
        List<Where> list = new ArrayList<>();
        for (Node n : array) {
            list.add(deserializeWhere(n));
        }
        return list.toArray(Where[]::new);
    }

    public static OrderBy[] deserializeOrderBys(Node arrayNode) throws DeserializationException {
        if (!(arrayNode instanceof ArrayNode array)) {
            return new OrderBy[0];
        }
        List<OrderBy> list = new ArrayList<>();
        for (Node n : array) {
            list.add(deserializeOrderBy(n));
        }
        return list.toArray(OrderBy[]::new);
    }

    private static OrderBy deserializeOrderBy(Node node) throws DeserializationException {
        if (!(node instanceof ObjectNode objNode)) {
            throw new DeserializationException("OrderBy node is not an object: " + node);
        }

        var selectorNode = objNode.get("selector");
        if (!(selectorNode instanceof ValueNode selectorVN)) {
            throw new DeserializationException("Missing selector in OrderBy");
        }

        var orderByTypeNode = objNode.get("orderByType");
        if (!(orderByTypeNode instanceof Node)) {
            throw new DeserializationException("Missing orderByType in OrderBy");
        }

        var orderByType = convertValue(orderByTypeNode, OrderByType.class);
        boolean useExpression = getBooleanOrDefault(objNode.get("useExpression"), false);

        return new OrderBy(selectorVN.asText(), orderByType, useExpression);
    }

    private static SkipIfInfo deserializeSkipIfInfo(Node node) {
        if (!(node instanceof ObjectNode objNode)) {
            return new SkipIfInfo(false, false, false, false);
        }

        boolean skipIfNull = getBooleanOrDefault(objNode.get("skipIfNull"), false);
        boolean skipIfEmptyList = getBooleanOrDefault(objNode.get("skipIfEmptyList"), false);
        boolean skipIfEmptyString = getBooleanOrDefault(objNode.get("skipIfEmptyString"), false);
        boolean skipIfBlankString = getBooleanOrDefault(objNode.get("skipIfBlankString"), false);

        return new SkipIfInfo(skipIfNull, skipIfEmptyList, skipIfEmptyString, skipIfBlankString);
    }

}

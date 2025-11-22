package cool.scx.data.serialization;

import dev.scx.data.aggregation.*;
import cool.scx.object.ScxObject;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;
import cool.scx.object.node.ValueNode;

import java.util.ArrayList;

public class AggregationDeserializer {

    public static Aggregation deserializeAggregationFromJson(String json) throws DeserializationException {
        try {
            var node = ScxObject.fromJson(json);
            return deserializeAggregation(node);
        } catch (Exception e) {
            throw new DeserializationException(e);
        }
    }

    public static Aggregation deserializeAggregation(Node v) throws DeserializationException {
        if (v == null || v.isNull() || !(v instanceof ObjectNode objNode)) {
            throw new DeserializationException("Aggregation node is null or not an ObjectNode");
        }

        var typeNode = objNode.get("@type");
        if (!(typeNode instanceof ValueNode vn) || !"Aggregation".equals(vn.asText())) {
            throw new DeserializationException("Unknown or missing @type for Aggregation: " + v);
        }

        var groupBysNode = objNode.get("groupBys");
        if (!(groupBysNode instanceof ArrayNode arrayGroupBys)) {
            throw new DeserializationException("groupBys node is not an ArrayNode: " + groupBysNode);
        }

        var groupByList = new ArrayList<GroupBy>();
        for (Node gbNode : arrayGroupBys) {
            groupByList.add(deserializeGroupBy(gbNode));
        }

        var aggsNode = objNode.get("aggs");
        if (!(aggsNode instanceof ArrayNode arrayAggs)) {
            throw new DeserializationException("aggs node is not an ArrayNode: " + aggsNode);
        }

        var aggList = new ArrayList<Agg>();
        for (Node aggNode : arrayAggs) {
            aggList.add(deserializeAgg(aggNode));
        }

        return new AggregationImpl()
                .aggs(aggList.toArray(Agg[]::new))
                .groupBys(groupByList.toArray(GroupBy[]::new));
    }

    private static GroupBy deserializeGroupBy(Node node) throws DeserializationException {
        if (node == null || node.isNull() || !(node instanceof ObjectNode objNode)) {
            throw new DeserializationException("Invalid GroupBy node");
        }

        var typeNode = objNode.get("@type");
        if (!(typeNode instanceof ValueNode vn)) {
            throw new DeserializationException("Missing or invalid @type in GroupBy node: " + node);
        }

        var type = vn.asText();
        return switch (type) {
            case "FieldGroupBy" -> deserializeFieldGroupBy(objNode);
            case "ExpressionGroupBy" -> deserializeExpressionGroupBy(objNode);
            default -> throw new DeserializationException("Unknown GroupBy type: " + type);
        };
    }

    private static FieldGroupBy deserializeFieldGroupBy(ObjectNode node) throws DeserializationException {
        var fieldNameNode = node.get("fieldName");
        if (!(fieldNameNode instanceof ValueNode vn)) {
            throw new DeserializationException("Missing or invalid fieldName in FieldGroupBy: " + node);
        }
        return new FieldGroupBy(vn.asText());
    }

    private static ExpressionGroupBy deserializeExpressionGroupBy(ObjectNode node) throws DeserializationException {
        var aliasNode = node.get("alias");
        var expressionNode = node.get("expression");

        if (!(aliasNode instanceof ValueNode aliasVN) || !(expressionNode instanceof ValueNode exprVN)) {
            throw new DeserializationException("Missing or invalid alias/expression in ExpressionGroupBy: " + node);
        }

        return new ExpressionGroupBy(aliasVN.asText(), exprVN.asText());
    }

    private static Agg deserializeAgg(Node node) throws DeserializationException {
        if (node == null || node.isNull() || !(node instanceof ObjectNode objNode)) {
            throw new DeserializationException("Invalid Agg node: " + node);
        }

        var typeNode = objNode.get("@type");
        if (!(typeNode instanceof ValueNode vn) || !"Agg".equals(vn.asText())) {
            throw new DeserializationException("Unknown or missing @type for Agg: " + node);
        }

        var aliasNode = objNode.get("alias");
        var expressionNode = objNode.get("expression");

        if (!(aliasNode instanceof ValueNode aliasVN) || !(expressionNode instanceof ValueNode exprVN)) {
            throw new DeserializationException("Missing or invalid alias/expression in Agg: " + node);
        }

        return new Agg(aliasVN.asText(), exprVN.asText());
    }

}

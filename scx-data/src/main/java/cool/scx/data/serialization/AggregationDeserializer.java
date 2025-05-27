package cool.scx.data.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.aggregation.*;

import java.util.ArrayList;

public class AggregationDeserializer {

    public static Aggregation deserializeAggregationFromJson(String json) throws DeserializationException {
        try {
            var v = ObjectUtils.jsonMapper().readTree(json);
            return deserializeAggregation(v);
        } catch (JsonProcessingException e) {
            throw new DeserializationException(e);
        }
    }

    public static Aggregation deserializeAggregation(JsonNode v) throws DeserializationException {
        if (v == null || v.isNull()) {
            throw new DeserializationException("Aggregation object is null or empty");
        }
        if (!v.isObject()) {
            throw new DeserializationException("FieldPolicy node is not an object: " + v);
        }
        var typeNode = v.get("@type");
        if (typeNode == null || !"Aggregation".equals(typeNode.asText())) {
            throw new DeserializationException("Unknown or missing @type for Aggregation: " + v);
        }

        var groupBysNode = v.get("groupBys");

        if (groupBysNode == null || !groupBysNode.isArray()) {
            throw new DeserializationException("FieldPolicy node is not an array: " + v);
        }

        var groupByList = new ArrayList<GroupBy>();
        for (JsonNode gbNode : groupBysNode) {
            groupByList.add(deserializeGroupBy(gbNode));
        }
        var groupBys = groupByList.toArray(GroupBy[]::new);


        var aggsNode = v.get("aggs");
        if (aggsNode == null || !aggsNode.isArray()) {
            throw new DeserializationException("FieldPolicy node is not an array: " + v);
        }
        var aggList = new ArrayList<Agg>();
        for (JsonNode aggNode : aggsNode) {
            aggList.add(deserializeAgg(aggNode));
        }
        var aggs = aggList.toArray(Agg[]::new);
        return new AggregationImpl().aggs(aggs).groupBys(groupBys);
    }

    private static GroupBy deserializeGroupBy(JsonNode node) throws DeserializationException {
        if (node == null || node.isNull()) {
            throw new DeserializationException("Invalid JSON for GroupBy");
        }
        var typeNode = node.get("@type");
        if (typeNode == null) {
            throw new DeserializationException("Unknown or missing @type for GroupBy: " + node);
        }
        var type = typeNode.asText();
        if (type.equals("FieldGroupBy")) {
            return deserializeFieldGroupBy(node);
        } else if (type.equals("ExpressionGroupBy")) {
            return deserializeExpressionGroupBy(node);
        }

        throw new DeserializationException("Unknown GroupBy type: " + type);
    }

    public static FieldGroupBy deserializeFieldGroupBy(JsonNode node) throws DeserializationException {
        //不允许为空
        var fieldNameNode = node.get("fieldName");
        if (fieldNameNode == null || fieldNameNode.isNull()) {
            throw new DeserializationException("Invalid JSON for FieldGroupBy");
        }
        var fieldName = fieldNameNode.asText();
        return new FieldGroupBy(fieldName);
    }

    public static ExpressionGroupBy deserializeExpressionGroupBy(JsonNode node) throws DeserializationException {
        var aliasNode = node.get("alias");
        if (aliasNode == null || aliasNode.isNull()) {
            throw new DeserializationException("Invalid JSON for ExpressionGroupBy");
        }
        var expressionNode = node.get("expression");
        if (expressionNode == null || expressionNode.isNull()) {
            throw new DeserializationException("Invalid JSON for ExpressionGroupBy");
        }
        var alias = aliasNode.asText();
        var expression = expressionNode.asText();
        return new ExpressionGroupBy(alias, expression);
    }

    private static Agg deserializeAgg(JsonNode node) throws DeserializationException {
        if (node == null || node.isNull()) {
            throw new DeserializationException("Invalid JSON for Agg");
        }
        var typeNode = node.get("@type");
        if (typeNode == null || !"Aggregation".equals(typeNode.asText())) {
            throw new DeserializationException("Unknown or missing @type for Agg: " + node);
        }
        //这两个都不允许为空
        var aliasNode = node.get("alias");
        if (aliasNode == null || !aliasNode.isTextual()) {
            throw new DeserializationException("Invalid JSON for Agg");
        }
        var expressionNode = node.get("expression");
        if (expressionNode == null || !expressionNode.isTextual()) {
            throw new DeserializationException("Invalid JSON for Agg");
        }
        var alias = aliasNode.asText();
        var expression = expressionNode.asText();
        return new Agg(alias, expression);
    }

}

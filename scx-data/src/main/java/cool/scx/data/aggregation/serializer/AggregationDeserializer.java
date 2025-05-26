package cool.scx.data.aggregation.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.aggregation.*;

import java.util.ArrayList;

public class AggregationDeserializer {

    public static final AggregationDeserializer AGGREGATION_DEFINITION_DESERIALIZER = new AggregationDeserializer();

    public Aggregation fromJson(String json) throws JsonProcessingException {
        var v = ObjectUtils.jsonMapper().readTree(json);
        return deserialize(v);
    }

    public Aggregation deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("Aggregation")) {
                return deserializeAggregationDefinition(v);
            }
        }
        throw new IllegalArgumentException("Invalid aggregation definition: " + v);
    }

    public Aggregation deserializeAggregationDefinition(JsonNode objectNode) {

        if (objectNode == null) {
            return new AggregationImpl();
        }

        var aggregationDefinition = new AggregationImpl();

        var groupBysNode = objectNode.get("groupBys");
        var aggsNode = objectNode.get("aggs");

        if (groupBysNode != null && !groupBysNode.isNull()) {
            var s = new ArrayList<GroupBy>();
            for (var groupByNode : groupBysNode) {
                s.add(deserializeGroupBy(groupByNode));
            }
            aggregationDefinition.groupBys(s.toArray(GroupBy[]::new));
        }

        if (aggsNode != null && !aggsNode.isNull()) {
            var s = new ArrayList<Agg>();
            for (var aggNode : aggsNode) {
                s.add(deserializeAgg(aggNode));
            }
            aggregationDefinition.aggs(s.toArray(Agg[]::new));
        }

        return aggregationDefinition;
    }

    public GroupBy deserializeGroupBy(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("FieldGroupBy")) {
                return deserializeFieldGroupBy(v);
            }
            if (type.equals("ExpressionGroupBy")) {
                return deserializeExpressionGroupBy(v);
            }
        }
        throw new IllegalArgumentException("Invalid Group By: " + v);
    }

    private GroupBy deserializeFieldGroupBy(JsonNode v) {
        var fieldNameNode = v.path("fieldName");
        var fieldName = fieldNameNode.asText();
        return new FieldGroupBy(fieldName);
    }

    private GroupBy deserializeExpressionGroupBy(JsonNode v) {
        var aliasNode = v.path("alias");
        var expressionNode = v.path("expression");
        var alias = aliasNode.asText();
        var expression = expressionNode.asText();
        return new ExpressionGroupBy(alias, expression);
    }

    public Agg deserializeAgg(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("Agg")) {
                return deserializeAgg0(v);
            }
        }
        throw new IllegalArgumentException("Invalid Group By: " + v);
    }

    private Agg deserializeAgg0(JsonNode v) {
        var aliasNode = v.path("alias");
        var expressionNode = v.path("expression");
        var alias = aliasNode.asText();
        var expression = expressionNode.asText();
        return new Agg(alias, expression);
    }

}

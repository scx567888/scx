package cool.scx.data.aggregation.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.aggregation.*;
import cool.scx.data.build_control.BuildControlInfo;

import java.util.ArrayList;

import static cool.scx.common.util.ObjectUtils.convertValue;

//todo 未完成
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
        var infoNode = v.path("info");
        var fieldName = fieldNameNode.asText();
        var info = convertValue(infoNode, BuildControlInfo.class);
        return new FieldGroupBy(fieldName, info);
    }

    private GroupBy deserializeExpressionGroupBy(JsonNode v) {
        var aliasNode = v.path("alias");
        var expressionNode = v.path("expression");
        var infoNode = v.path("info");
        var alias = aliasNode.asText();
        var expression = expressionNode.asText();
        var info = convertValue(infoNode, BuildControlInfo.class);
        return new ExpressionGroupBy(alias, expression, info);
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
        var expressionNode = v.path("expression");
        var aliasNode = v.path("alias");
        var infoNode = v.path("info");
        var name = expressionNode.asText();
        var expression = aliasNode == null || aliasNode.isNull() ? null : aliasNode.asText();
        var info = convertValue(infoNode, BuildControlInfo.class);
        return new Agg(name, expression, info);
    }

}

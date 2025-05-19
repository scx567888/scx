package cool.scx.data.aggregation.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.aggregation.AggregationImpl;
import cool.scx.data.aggregation.GroupBy;
import cool.scx.data.aggregation.GroupByOption;

import java.util.Map;

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
            for (var groupByNode : groupBysNode) {
                aggregationDefinition.groupBy(deserializeGroupBy(groupByNode));
            }
        }

        if (aggsNode != null && !aggsNode.isNull()) {
            var aggregateColumns = convertValue(objectNode.get("aggs"), new TypeReference<Map<String, String>>() {});
            for (var aggregateColumn : aggregateColumns.entrySet()) {
                aggregationDefinition.agg(aggregateColumn.getKey(), aggregateColumn.getValue());
            }
        }

        return aggregationDefinition;
    }

    public GroupBy deserializeGroupBy(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("GroupBy")) {
                return deserializeGroupBy0(v);
            }
        }
        throw new IllegalArgumentException("Invalid Group By: " + v);
    }

    private GroupBy deserializeGroupBy0(JsonNode v) {
        var nameNode = v.path("name");
        var expressionNode = v.path("expression");
        var infoNode = v.path("info");
        var name = nameNode.asText();
        var expression = expressionNode == null || expressionNode.isNull() ? null : expressionNode.asText();
        var info = convertValue(infoNode, GroupByOption.Info.class);
        return new GroupBy(name, expression, info);
    }

}

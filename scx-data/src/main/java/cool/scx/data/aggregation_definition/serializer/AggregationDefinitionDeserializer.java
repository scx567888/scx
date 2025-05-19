package cool.scx.data.aggregation_definition.serializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.aggregation_definition.AggregationDefinition;
import cool.scx.data.aggregation_definition.AggregationDefinitionImpl;

import java.util.Map;

import static cool.scx.common.util.ObjectUtils.convertValue;

public class AggregationDefinitionDeserializer {

    public static final AggregationDefinitionDeserializer AGGREGATION_DEFINITION_DESERIALIZER = new AggregationDefinitionDeserializer();

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("AggregationDefinition")) {
                return deserializeAggregationDefinition(v);
            }
        }
        return v;
    }

    public AggregationDefinition deserializeAggregationDefinition(JsonNode objectNode) {

        if (objectNode == null) {
            return new AggregationDefinitionImpl();
        }

        var aggregationDefinition = new AggregationDefinitionImpl();

        if (objectNode.get("groupBys") != null && !objectNode.get("groupBys").isNull()) {
            var groupBys = convertValue(objectNode.get("groupBys"), new TypeReference<Map<String, String>>() {});
            for (var groupBy : groupBys.entrySet()) {
                aggregationDefinition.groupBy(groupBy.getKey(), groupBy.getValue());
            }
        }

        if (objectNode.get("aggregateColumns") != null && !objectNode.get("aggregateColumns").isNull()) {
            var aggregateColumns = convertValue(objectNode.get("aggregateColumns"), new TypeReference<Map<String, String>>() {});
            for (var aggregateColumn : aggregateColumns.entrySet()) {
                aggregationDefinition.aggregateColumn(aggregateColumn.getKey(), aggregateColumn.getValue());
            }
        }

        return aggregationDefinition;
    }

}

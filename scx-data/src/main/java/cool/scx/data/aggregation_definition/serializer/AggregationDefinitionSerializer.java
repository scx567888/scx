package cool.scx.data.aggregation_definition.serializer;

import cool.scx.data.aggregation_definition.AggregationDefinition;

import java.util.LinkedHashMap;

public class AggregationDefinitionSerializer {

    public static final AggregationDefinitionSerializer AGGREGATION_DEFINITION_SERIALIZER = new AggregationDefinitionSerializer();

    public Object serialize(Object obj) {
        return switch (obj) {
            case AggregationDefinition s -> serializeAggregationDefinition(s);
            default -> obj;
        };
    }

    public LinkedHashMap<String, Object> serializeAggregationDefinition(AggregationDefinition aggregationDefinition) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "AggregationDefinition");
        m.put("groupBys", aggregationDefinition.groupBys());
        m.put("aggregateColumns", aggregationDefinition.aggregateColumns());
        return m;
    }

}

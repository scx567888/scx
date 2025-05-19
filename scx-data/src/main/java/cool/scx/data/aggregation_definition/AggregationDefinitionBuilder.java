package cool.scx.data.aggregation_definition;

public class AggregationDefinitionBuilder {

    public static AggregationDefinition groupBy(String fieldName) {
        return new AggregationDefinitionImpl().groupBy(fieldName);
    }

    public static AggregationDefinition groupBy(String fieldName, GroupByOption... options) {
        return new AggregationDefinitionImpl().groupBy(fieldName, options);
    }

    public static AggregationDefinition groupBy(String fieldName, String expression) {
        return new AggregationDefinitionImpl().groupBy(fieldName, expression);
    }

    public static AggregationDefinition agg(String fieldName, String expression) {
        return new AggregationDefinitionImpl().agg(fieldName, expression);
    }

}

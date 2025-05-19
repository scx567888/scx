package cool.scx.data.aggregation;

public class AggregationBuilder {

    public static Aggregation groupBy(String fieldName) {
        return new AggregationImpl().groupBy(fieldName);
    }

    public static Aggregation groupBy(String fieldName, GroupByOption... options) {
        return new AggregationImpl().groupBy(fieldName, options);
    }

    public static Aggregation groupBy(String fieldName, String expression) {
        return new AggregationImpl().groupBy(fieldName, expression);
    }

    public static Aggregation agg(String fieldName, String expression) {
        return new AggregationImpl().agg(fieldName, expression);
    }

}

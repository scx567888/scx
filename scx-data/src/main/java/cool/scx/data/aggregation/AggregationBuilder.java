package cool.scx.data.aggregation;

/// AggregationBuilder
public class AggregationBuilder {

    public static AggregationImpl aggregation() {
        return new AggregationImpl();
    }

    public static GroupBy groupBy(String fieldName) {
        return new FieldGroupBy(fieldName);
    }

    public static GroupBy groupBy(String alias, String expression) {
        return new ExpressionGroupBy(alias, expression);
    }

    public static Agg agg(String alias, String expression) {
        return new Agg(alias, expression);
    }

}

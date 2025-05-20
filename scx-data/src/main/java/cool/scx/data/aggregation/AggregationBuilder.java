package cool.scx.data.aggregation;

public class AggregationBuilder {

    public static AggregationImpl aggregation() {
        return new AggregationImpl();
    }

    public static GroupBy groupBy(String fieldName) {
        return new GroupBy(fieldName, null);
    }

    public static Aggregation agg(String fieldName, String expression) {
        return new Agg(fieldName, expression);
    }

}

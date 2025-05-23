package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;

/// AggregationBuilder
public class AggregationBuilder {

    public static AggregationImpl aggregation() {
        return new AggregationImpl();
    }

    public static GroupBy groupBy(String fieldName, BuildControl... controls) {
        return new FieldGroupBy(fieldName, controls);
    }

    public static GroupBy groupBy(String alias, String expression, BuildControl... controls) {
        return new ExpressionGroupBy(alias, expression, controls);
    }

    public static Agg agg(String alias, String expression, BuildControl... controls) {
        return new Agg(alias, expression, controls);
    }

}

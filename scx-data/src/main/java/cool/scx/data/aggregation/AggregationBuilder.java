package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;

/// AggregationBuilder
public class AggregationBuilder {

    public static AggregationImpl aggregation() {
        return new AggregationImpl();
    }

    public static GroupBy groupBy(String selector, BuildControl... controls) {
        return new GroupBy(selector, null, controls);
    }

    public static GroupBy groupBy(String selector, String alias, BuildControl... controls) {
        return new GroupBy(selector, alias, controls);
    }

    public static Agg agg(String alias, String expression, BuildControl... controls) {
        return new Agg(expression, alias, controls);
    }

}

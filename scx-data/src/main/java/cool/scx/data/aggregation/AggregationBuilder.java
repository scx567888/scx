package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;

/// todo 参数有点小问题
public class AggregationBuilder {

    public static AggregationImpl aggregation() {
        return new AggregationImpl();
    }

    public static GroupBy groupBy(String fieldName, BuildControl... controls) {
        return new GroupBy(fieldName, null, controls);
    }

    public static Agg agg(String fieldName, String expression) {
        return new Agg(fieldName, expression);
    }

}

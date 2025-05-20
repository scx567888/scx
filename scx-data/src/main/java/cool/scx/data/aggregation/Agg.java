package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;
import cool.scx.data.build_control.BuildControlInfo;

import static cool.scx.data.build_control.BuildControlInfo.ofInfo;

public class Agg extends AggregationLike<Agg> {

    private final String expression;
    private final String alias;
    private final BuildControlInfo info;

    public Agg(String expression, String alias, BuildControlInfo info) {
        if (expression == null) {
            throw new IllegalArgumentException("Agg expression cannot be null");
        }
        this.expression = expression;
        this.alias = alias;
        this.info = info;
    }

    public Agg(String expression, String alias, BuildControl... controls) {
        this(expression, alias, ofInfo(controls));
    }

    public String expression() {
        return expression;
    }

    public String alias() {
        return alias;
    }

    public BuildControlInfo info() {
        return info;
    }

    @Override
    public AggregationImpl toAggregation() {
        return new AggregationImpl().agg(this);
    }

}

package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;
import cool.scx.data.build_control.BuildControlInfo;

import static cool.scx.data.build_control.BuildControlInfo.ofInfo;

public class Agg extends AggregationLike<Agg> {

    private final String alias;
    private final String expression;
    private final BuildControlInfo info;

    public Agg(String alias, String expression, BuildControlInfo info) {
        if (alias == null) {
            throw new NullPointerException("alias is null");
        }
        if (expression == null) {
            throw new NullPointerException("Agg expression cannot be null");
        }
        this.alias = alias;
        this.expression = expression;
        this.info = info;
    }

    public Agg(String alias, String expression, BuildControl... controls) {
        this(alias, expression, ofInfo(controls));
    }

    public String alias() {
        return alias;
    }

    public String expression() {
        return expression;
    }

    public BuildControlInfo info() {
        return info;
    }

    @Override
    protected AggregationImpl toAggregation() {
        return new AggregationImpl().aggs(this);
    }

}

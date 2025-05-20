package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;

@SuppressWarnings("unchecked")
public abstract class AggregationLike<AL extends AggregationLike<AL>> implements Aggregation {

    private AggregationImpl aggregation;

    private AggregationImpl aggregation() {
        if (aggregation == null) {
            aggregation = toAggregation();
        }
        return aggregation;
    }

    @Override
    public AL groupBys(GroupBy... groupBys) {
        aggregation().groupBys(groupBys);
        return (AL) this;
    }

    @Override
    public AL aggs(Agg... aggs) {
        aggregation().aggs(aggs);
        return (AL) this;
    }

    @Override
    public GroupBy[] getGroupBys() {
        return aggregation().getGroupBys();
    }

    @Override
    public Agg[] getAggs() {
        return aggregation().getAggs();
    }

    @Override
    public AL clearGroupBys() {
        aggregation().clearGroupBys();
        return (AL) this;
    }

    @Override
    public AL clearAggs() {
        aggregation().clearAggs();
        return (AL) this;
    }

    @Override
    public AL groupBy(String selector, BuildControl... controls) {
        aggregation().groupBy(selector, controls);
        return (AL) this;
    }

    @Override
    public AL groupBy(String selector, String alias, BuildControl... controls) {
        aggregation().groupBy(selector, alias, controls);
        return (AL) this;
    }

    @Override
    public AL agg(String expression, BuildControl... controls) {
        aggregation().agg(expression, controls);
        return (AL) this;
    }

    @Override
    public AL agg(String expression, String alias, BuildControl... controls) {
        aggregation().agg(expression, alias, controls);
        return (AL) this;
    }

    protected abstract AggregationImpl toAggregation();

}

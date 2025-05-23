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
    public AL groupBy(String fieldName, BuildControl... controls) {
        aggregation().groupBy(fieldName, controls);
        return (AL) this;
    }

    @Override
    public AL groupBy(String alias, String expression, BuildControl... controls) {
        aggregation().groupBy(alias, expression, controls);
        return (AL) this;
    }

    @Override
    public AL agg(String alias, String expression, BuildControl... controls) {
        aggregation().agg(alias, expression, controls);
        return (AL) this;
    }

    protected abstract AggregationImpl toAggregation();

}

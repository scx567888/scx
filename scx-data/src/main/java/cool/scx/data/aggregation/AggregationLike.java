package cool.scx.data.aggregation;

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
    public AL groupBy(GroupBy... groupBy) {
        aggregation().groupBy(groupBy);
        return (AL) this;
    }

    @Override
    public AL agg(Agg... aggs) {
        aggregation().agg(aggs);
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

    public abstract AggregationImpl toAggregation();

}

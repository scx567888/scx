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
    public AL groupBy(String name) {
        aggregation().groupBy(name);
        return (AL) this;
    }

    @Override
    public AL agg(String name, String value) {
        aggregation().agg(name, value);
        return (AL) this;
    }

    protected abstract AggregationImpl toAggregation();

}

package cool.scx.data.aggregation;

public class AggregationImpl implements Aggregation {

    private GroupBy[] groupBys;
    private Agg[] aggs;

    public AggregationImpl() {
        this.groupBys = new GroupBy[]{};
        this.aggs = new Agg[]{};
    }

    @Override
    public AggregationImpl groupBy(GroupBy... groupBys) {
        this.groupBys = groupBys;
        return this;
    }

    @Override
    public AggregationImpl agg(Agg... aggs) {
        this.aggs = aggs;
        return this;
    }

    @Override
    public GroupBy[] getGroupBys() {
        return groupBys;
    }

    @Override
    public Agg[] getAggs() {
        return aggs;
    }

    @Override
    public AggregationImpl clearGroupBys() {
        groupBys = new GroupBy[]{};
        return this;
    }

    @Override
    public AggregationImpl clearAggs() {
        aggs = new Agg[]{};
        return this;
    }

}

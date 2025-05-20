package cool.scx.data.aggregation;

public class AggregationImpl implements Aggregation {

    private GroupBy[] groupBys;
    private Agg[] aggs;

    public AggregationImpl() {
        this.groupBys = new GroupBy[]{};
        this.aggs = new Agg[]{};
    }

    @Override
    public Aggregation groupBy(GroupBy... groupBys) {
        this.groupBys = groupBys;
        return this;
    }

    @Override
    public Aggregation agg(Agg... aggs) {
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
    public Aggregation clearGroupBys() {
        groupBys = new GroupBy[]{};
        return this;
    }

    @Override
    public Aggregation clearAggs() {
        aggs = new Agg[]{};
        return this;
    }

}

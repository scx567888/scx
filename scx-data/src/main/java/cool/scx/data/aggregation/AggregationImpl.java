package cool.scx.data.aggregation;

import java.util.ArrayList;
import java.util.List;

public class AggregationImpl implements Aggregation {

    private List<GroupBy> groupBys;
    private List<Agg> aggs;

    public AggregationImpl() {
        this.groupBys = new ArrayList<>();
        this.aggs = new ArrayList<>();
    }

    @Override
    public AggregationImpl groupBys(GroupBy... groupBys) {
        this.groupBys = new ArrayList<>(List.of(groupBys));
        return this;
    }

    @Override
    public AggregationImpl aggs(Agg... aggs) {
        this.aggs = new ArrayList<>(List.of(aggs));
        return this;
    }

    @Override
    public GroupBy[] getGroupBys() {
        return groupBys.toArray(GroupBy[]::new);
    }

    @Override
    public Agg[] getAggs() {
        return aggs.toArray(Agg[]::new);
    }

    @Override
    public AggregationImpl clearGroupBys() {
        groupBys.clear();
        return this;
    }

    @Override
    public AggregationImpl clearAggs() {
        aggs.clear();
        return this;
    }

    @Override
    public AggregationImpl groupBy(String name) {
        groupBys.add(new GroupBy(name,null));
        return this;
    }

    @Override
    public AggregationImpl agg(String name, String value) {
        aggs.add(new Agg(name, value));
        return this;
    }

}

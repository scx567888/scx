package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;

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
    public AggregationImpl groupBy(String selector, BuildControl... controls) {
        groupBys.add(new GroupBy(selector, null, controls));
        return this;
    }

    @Override
    public AggregationImpl groupBy(String selector, String alias, BuildControl... controls) {
        groupBys.add(new GroupBy(selector, alias, controls));
        return this;
    }

    @Override
    public AggregationImpl agg(String alias, String expression, BuildControl... controls) {
        aggs.add(new Agg(alias, expression, controls));
        return this;
    }

}

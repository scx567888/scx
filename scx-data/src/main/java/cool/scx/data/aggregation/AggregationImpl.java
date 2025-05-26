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
    public AggregationImpl groupBy(String fieldName) {
        groupBys.add(AggregationBuilder.groupBy(fieldName));
        return this;
    }

    @Override
    public AggregationImpl groupBy(String alias, String expression) {
        groupBys.add(AggregationBuilder.groupBy(alias, expression));
        return this;
    }

    @Override
    public AggregationImpl agg(String alias, String expression) {
        aggs.add(AggregationBuilder.agg(alias, expression));
        return this;
    }

}

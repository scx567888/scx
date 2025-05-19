package cool.scx.data.aggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregationImpl implements Aggregation {

    private final List<GroupBy> groupBys;
    private final Map<String, String> aggs;

    public AggregationImpl() {
        this.groupBys = new ArrayList<>();
        this.aggs = new HashMap<>();
    }

    @Override
    public Aggregation groupBy(GroupBy groupBy) {
        groupBys.add(groupBy);
        return this;
    }

    @Override
    public GroupBy[] groupBys() {
        return groupBys.toArray(GroupBy[]::new);
    }

    @Override
    public Aggregation removeGroupBy(String fieldName) {
        groupBys.removeIf(c -> c.name().equals(fieldName));
        return this;
    }

    @Override
    public Aggregation clearGroupBys() {
        groupBys.clear();
        return this;
    }

    @Override
    public Aggregation agg(String fieldName, String expression) {
        aggs.put(fieldName, expression);
        return this;
    }

    @Override
    public Map<String, String> aggs() {
        return aggs;
    }

    @Override
    public Aggregation removeAgg(String fieldName) {
        aggs.remove(fieldName);
        return this;
    }

    @Override
    public Aggregation clearAggs() {
        aggs.clear();
        return this;
    }

}

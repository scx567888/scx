package cool.scx.data.aggregation;

import java.util.HashMap;
import java.util.Map;

public class AggregationImpl implements Aggregation {

    private final Map<String, GroupBy> groupBys;
    private final Map<String, String> aggs;

    public AggregationImpl() {
        this.groupBys = new HashMap<>();
        this.aggs = new HashMap<>();
    }

    @Override
    public Aggregation groupBy(String fieldName) {
        groupBys.put(fieldName, new GroupBy(fieldName, null));
        return this;
    }

    @Override
    public Aggregation groupBy(String fieldName, GroupByOption... groupByOptions) {
        groupBys.put(fieldName, new GroupBy(fieldName, null, groupByOptions));
        return this;
    }

    @Override
    public Aggregation groupBy(String fieldName, String expression) {
        groupBys.put(fieldName, new GroupBy(fieldName, expression));
        return this;
    }

    @Override
    public Map<String, GroupBy> groupBys() {
        return groupBys;
    }

    @Override
    public Aggregation removeGroupBy(String fieldName) {
        groupBys.remove(fieldName);
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

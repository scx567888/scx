package cool.scx.data.aggregation_definition;

import java.util.HashMap;
import java.util.Map;

public class AggregationDefinitionImpl implements AggregationDefinition {

    private final Map<String, GroupBy> groupBys;
    private final Map<String, String> aggregateColumns;

    public AggregationDefinitionImpl() {
        this.groupBys = new HashMap<>();
        this.aggregateColumns = new HashMap<>();
    }

    @Override
    public AggregationDefinition groupBy(String fieldName) {
        groupBys.put(fieldName, new GroupBy(fieldName, null));
        return this;
    }

    @Override
    public AggregationDefinition groupBy(String fieldName, GroupByOption... groupByOptions) {
        groupBys.put(fieldName, new GroupBy(fieldName, null, groupByOptions));
        return this;
    }

    @Override
    public AggregationDefinition groupBy(String fieldName, String expression) {
        groupBys.put(fieldName, new GroupBy(fieldName, expression));
        return this;
    }

    @Override
    public Map<String, GroupBy> groupBys() {
        return groupBys;
    }

    @Override
    public AggregationDefinition removeGroupBy(String fieldName) {
        groupBys.remove(fieldName);
        return this;
    }

    @Override
    public AggregationDefinition clearGroupBys() {
        groupBys.clear();
        return this;
    }

    @Override
    public AggregationDefinition aggregateColumn(String fieldName, String expression) {
        aggregateColumns.put(fieldName, expression);
        return this;
    }

    @Override
    public Map<String, String> aggregateColumns() {
        return aggregateColumns;
    }

    @Override
    public AggregationDefinition removeAggregateColumn(String fieldName) {
        aggregateColumns.remove(fieldName);
        return this;
    }

    @Override
    public AggregationDefinition clearAggregateColumns() {
        aggregateColumns.clear();
        return this;
    }

}

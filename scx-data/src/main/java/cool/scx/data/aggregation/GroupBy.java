package cool.scx.data.aggregation;

public abstract sealed class GroupBy extends AggregationLike<GroupBy> permits ExpressionGroupBy, FieldGroupBy {

    @Override
    protected AggregationImpl toAggregation() {
        return new AggregationImpl().groupBys(this);
    }

}

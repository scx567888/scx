package cool.scx.data.aggregation;

public class Agg extends AggregationLike<Agg> {

    private final String alias;
    private final String expression;

    public Agg(String alias, String expression) {
        if (alias == null) {
            throw new NullPointerException("alias is null");
        }
        if (expression == null) {
            throw new NullPointerException("Agg expression cannot be null");
        }
        this.alias = alias;
        this.expression = expression;
    }

    public String alias() {
        return alias;
    }

    public String expression() {
        return expression;
    }

    @Override
    protected AggregationImpl toAggregation() {
        return new AggregationImpl().aggs(this);
    }

}

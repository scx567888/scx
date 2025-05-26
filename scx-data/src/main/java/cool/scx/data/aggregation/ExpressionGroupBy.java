package cool.scx.data.aggregation;

public final class ExpressionGroupBy extends GroupBy {

    private final String alias;
    private final String expression;

    public ExpressionGroupBy(String alias, String expression) {
        //名称不能为空
        if (alias == null) {
            throw new NullPointerException("GroupBy 参数错误 : alias 不能为空 !!!");
        }
        if (expression == null) {
            throw new NullPointerException("GroupBy  : expression can not be null !!!");
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

}

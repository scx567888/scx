package cool.scx.data.query;

import static cool.scx.common.util.StringUtils.isBlank;

/// Where
///
/// @author scx567888
/// @version 0.0.1
public final class Condition extends QueryLike<Condition> implements Where {

    // fieldName 或者 表达式
    private final String selector;
    private final ConditionType conditionType;
    private final Object value1;
    private final Object value2;
    private final boolean useExpression;
    private final boolean useExpressionValue;

    public Condition(String selector, ConditionType conditionType, Object value1, Object value2, boolean useExpression, boolean useExpressionValue) {
        //名称不能为空
        if (isBlank(selector)) {
            throw new IllegalArgumentException("Condition 参数错误 : selector 不能为空 !!!");
        }
        //类型也不能为空
        if (conditionType == null) {
            throw new IllegalArgumentException("Condition 参数错误 : conditionType 不能为空 !!!");
        }
        this.selector = selector;
        this.conditionType = conditionType;
        this.value1 = value1;
        this.value2 = value2;
        this.useExpression = useExpression;
        this.useExpressionValue = useExpressionValue;
    }

    private Condition() {
        this.selector = null;
        this.conditionType = null;
        this.value1 = null;
        this.value2 = null;
        this.useExpression = false;
        this.useExpressionValue = false;    
    }

    public static Condition empty() {
        return new Condition();
    }

    public String selector() {
        return selector;
    }

    public ConditionType conditionType() {
        return conditionType;
    }

    public Object value1() {
        return value1;
    }

    public Object value2() {
        return value2;
    }

    public boolean useExpression() {
        return useExpression;
    }

    public boolean useExpressionValue() {
        return useExpressionValue;
    }

    public boolean isEmpty() {
        return conditionType == null;
    }

    @Override
    protected QueryImpl toQuery() {
        return new QueryImpl().where(this);
    }

}

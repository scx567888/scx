package cool.scx.data.query;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.query.ConditionType.BETWEEN;
import static cool.scx.data.query.ConditionType.NOT_BETWEEN;

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
    private final SkipIfInfo skipIfInfo;

    public Condition(String selector, ConditionType conditionType, Object value1, Object value2, boolean useExpression, boolean useExpressionValue, SkipIfInfo skipIfInfo) {
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
        this.skipIfInfo = skipIfInfo;
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

    public SkipIfInfo skipIfInfo() {
        return skipIfInfo;
    }

    @Override
    public boolean isEmpty() {
        if (conditionType == BETWEEN || conditionType == NOT_BETWEEN) {
            return skipIfInfo.shouldSkip(value1, value2);
        }
        return skipIfInfo.shouldSkip(value1);
    }

    @Override
    protected QueryImpl toQuery() {
        return new QueryImpl().where(this);
    }

}

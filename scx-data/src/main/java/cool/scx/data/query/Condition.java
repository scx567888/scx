package cool.scx.data.query;

import cool.scx.data.build_control.BuildControl;
import cool.scx.data.build_control.BuildControlInfo;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.build_control.BuildControlInfo.ofInfo;

/// Where
///
/// @author scx567888
/// @version 0.0.1
public final class Condition extends QueryLike<Condition> {

    // fieldName 或者 表达式
    private final String selector;
    private final ConditionType conditionType;
    private final Object value1;
    private final Object value2;
    private final BuildControlInfo info;

    public Condition(String selector, ConditionType conditionType, Object value1, Object value2, BuildControlInfo info) {
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
        this.info = info;
    }

    public Condition(String selector, ConditionType conditionType, Object value1, Object value2, BuildControl... controls) {
        this(selector, conditionType, value1, value2, ofInfo(controls));
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

    public BuildControlInfo info() {
        return info;
    }

    @Override
    protected QueryImpl toQuery() {
        return new QueryImpl().where(this);
    }

}

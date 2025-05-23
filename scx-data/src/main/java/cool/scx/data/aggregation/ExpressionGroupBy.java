package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;
import cool.scx.data.build_control.BuildControlInfo;

import static cool.scx.data.build_control.BuildControlInfo.ofInfo;

public non-sealed class ExpressionGroupBy extends GroupBy {

    private final String alias;
    private final String expression;
    private final BuildControlInfo info;

    public ExpressionGroupBy(String alias, String expression, BuildControlInfo info) {
        //名称不能为空
        if (alias == null) {
            throw new NullPointerException("GroupBy 参数错误 : alias 不能为空 !!!");
        }
        if (expression == null) {
            throw new NullPointerException("GroupBy  : expression can not be null !!!");
        }
        this.alias = alias;
        this.expression = expression;
        this.info = info;
    }

    public ExpressionGroupBy(String alias, String expression, BuildControl... controls) {
        this(alias, expression, ofInfo(controls));
    }

    public String alias() {
        return alias;
    }

    public String expression() {
        return expression;
    }

    public BuildControlInfo info() {
        return info;
    }

}

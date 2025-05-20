package cool.scx.data.query;

import cool.scx.data.build_control.BuildControl;
import cool.scx.data.build_control.BuildControlInfo;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.build_control.BuildControlInfo.ofInfo;

/// Where
///
/// @author scx567888
/// @version 0.0.1
public final class Where extends QueryLike<Where> {

    // fieldName 或者 表达式
    private final String selector;
    private final WhereType whereType;
    private final Object value1;
    private final Object value2;
    private final BuildControlInfo info;

    public Where(String selector, WhereType whereType, Object value1, Object value2, BuildControlInfo info) {
        //名称不能为空
        if (isBlank(selector)) {
            throw new IllegalArgumentException("Where 参数错误 : selector 不能为空 !!!");
        }
        //类型也不能为空
        if (whereType == null) {
            throw new IllegalArgumentException("Where 参数错误 : whereType 不能为空 !!!");
        }
        this.selector = selector;
        this.whereType = whereType;
        this.value1 = value1;
        this.value2 = value2;
        this.info = info;
    }

    public Where(String selector, WhereType whereType, Object value1, Object value2, BuildControl... controls) {
        this(selector, whereType, value1, value2, ofInfo(controls));
    }

    public String selector() {
        return selector;
    }

    public WhereType whereType() {
        return whereType;
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
    protected Query toQuery() {
        return new QueryImpl().where(this);
    }

}

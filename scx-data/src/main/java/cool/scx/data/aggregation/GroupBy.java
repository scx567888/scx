package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;
import cool.scx.data.build_control.BuildControlInfo;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.build_control.BuildControlInfo.ofInfo;

public class GroupBy extends AggregationLike<GroupBy>{

    private final String selector;
    private final String alias;
    private final BuildControlInfo info;

    public GroupBy(String selector, String alias, BuildControlInfo info) {
        //名称不能为空
        if (isBlank(selector)) {
            throw new IllegalArgumentException("GroupBy 参数错误 : selector 不能为空 !!!");
        }
        this.selector = selector;
        this.alias = alias;
        this.info = info;
    }

    public GroupBy(String name, String alias, BuildControl... controls) {
        this(name, alias, ofInfo(controls));
    }

    public String selector() {
        return selector;
    }

    public String alias() {
        return alias;
    }

    public BuildControlInfo info() {
        return info;
    }

    @Override
    public AggregationImpl toAggregation() {
        return new AggregationImpl().groupBy(this);
    }
    
}

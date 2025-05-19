package cool.scx.data.aggregation_definition;

import cool.scx.data.aggregation_definition.GroupByOption.Info;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.aggregation_definition.GroupByOption.ofInfo;

public class GroupBy {

    private final String name;
    private final String expression;
    private final Info info;

    public GroupBy(String name, String expression, Info info) {
        //名称不能为空
        if (isBlank(name)) {
            throw new IllegalArgumentException("GroupBy 参数错误 : 名称 不能为空 !!!");
        }
        this.name = name;
        this.expression = expression;
        this.info = info;
    }

    public GroupBy(String name, String expression, GroupByOption... options) {
        this(name, expression, ofInfo(options));
    }


    public String name() {
        return name;
    }

    public String expression() {
        return expression;
    }

    public Info info() {
        return info;
    }

}

package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;

/// 聚合定义
public interface Aggregation {

    /// 设置 分组
    Aggregation groupBys(GroupBy... groupBy);

    /// 设置 聚合列
    Aggregation aggs(Agg... aggs);

    /// 获取 分组
    GroupBy[] getGroupBys();

    /// 获取 聚合列
    Agg[] getAggs();

    /// 清除 所有分组
    Aggregation clearGroupBys();

    /// 清除 所有聚合列
    Aggregation clearAggs();

    /// 追加 分组
    Aggregation groupBy(String selector, BuildControl... controls);

    /// 追加 分组
    Aggregation groupBy(String selector, String alias, BuildControl... controls);

    /// 追加 聚合
    Aggregation agg(String expression, BuildControl... controls);

    /// 追加 聚合
    Aggregation agg(String expression, String alias, BuildControl... controls);

}

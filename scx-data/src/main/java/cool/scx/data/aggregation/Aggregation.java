package cool.scx.data.aggregation;

/// 聚合定义
public interface Aggregation {

    /// 设置分组
    Aggregation groupBy(GroupBy... groupBy);

    /// 添加聚合列
    Aggregation agg(Agg... aggs);

    /// 获取 分组列
    GroupBy[] getGroupBys();

    /// 获取聚合列
    Agg[] getAggs();

    /// 清除 所有分组
    Aggregation clearGroupBys();

    /// 清除 所有聚合列
    Aggregation clearAggs();

}

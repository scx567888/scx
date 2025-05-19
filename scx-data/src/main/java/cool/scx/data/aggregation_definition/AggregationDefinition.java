package cool.scx.data.aggregation_definition;

import java.util.Map;

/// 聚合定义
public interface AggregationDefinition {

    /// 设置分组
    AggregationDefinition groupBy(String fieldName);

    /// 设置分组
    AggregationDefinition groupBy(String fieldName, GroupByOption... groupByOptions);

    /// 设置分组
    AggregationDefinition groupBy(String fieldName, String expression);

    /// 获取 分组列
    Map<String, GroupBy> groupBys();

    /// 移除 某个分组
    AggregationDefinition removeGroupBy(String fieldName);

    /// 清除 所有分组
    AggregationDefinition clearGroupBys();

    /// 设置聚合列
    AggregationDefinition aggregateColumn(String fieldName, String expression);

    /// 获取聚合列
    Map<String, String> aggregateColumns();

    /// 清除 某个聚合列
    AggregationDefinition removeAggregateColumn(String fieldName);

    /// 清除 所有聚合列
    AggregationDefinition clearAggregateColumns();

}

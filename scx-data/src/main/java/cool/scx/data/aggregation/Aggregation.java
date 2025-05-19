package cool.scx.data.aggregation;

import java.util.Map;

/// 聚合定义
public interface Aggregation {

    /// 设置分组
    Aggregation groupBy(GroupBy groupBy);

    /// 获取 分组列
    GroupBy[] groupBys();

    /// 移除 某个分组
    Aggregation removeGroupBy(String fieldName);

    /// 清除 所有分组
    Aggregation clearGroupBys();

    /// 设置聚合列
    Aggregation agg(String fieldName, String expression);

    /// 获取聚合列
    Map<String, String> aggs();

    /// 清除 某个聚合列
    Aggregation removeAgg(String fieldName);

    /// 清除 所有聚合列
    Aggregation clearAggs();

    /// 设置分组
    default Aggregation groupBy(String fieldName) {
        return this.groupBy(new GroupBy(fieldName, null));
    }

    /// 设置分组
    default Aggregation groupBy(String fieldName, GroupByOption... groupByOptions) {
        return this.groupBy(new GroupBy(fieldName, null, groupByOptions));
    }

    /// 设置分组
    default Aggregation groupBy(String fieldName, String expression) {
        return this.groupBy(new GroupBy(fieldName, expression));
    }

}

package cool.scx.data;

import cool.scx.data.aggregation_definition.AggregationDefinition;
import cool.scx.data.query.Query;

import java.util.List;
import java.util.Map;

import static cool.scx.data.query.QueryBuilder.query;

/// 拥有聚合能力的 Repository
///
/// @param <Entity>
/// @param <ID>
public interface AggregatableRepository<Entity, ID> extends Repository<Entity, ID> {

    /// 创建一个数据聚合器
    ///
    /// @param beforeAggregateQuery  聚合前 Query
    /// @param aggregationDefinition 聚合定义
    /// @param afterAggregateQuery   聚合后 Query
    /// @return 聚合器
    Aggregator aggregator(Query beforeAggregateQuery, AggregationDefinition aggregationDefinition, Query afterAggregateQuery);

    default Aggregator aggregator(AggregationDefinition aggregationDefinition, Query afterAggregateQuery) {
        return aggregator(query(), aggregationDefinition, afterAggregateQuery);
    }

    default Aggregator aggregator(Query beforeAggregateQuery, AggregationDefinition aggregationDefinition) {
        return aggregator(beforeAggregateQuery, aggregationDefinition, query());
    }

    default Aggregator aggregator(AggregationDefinition aggregationDefinition) {
        return aggregator(query(), aggregationDefinition, query());
    }

    default List<Map<String, Object>> aggregate(Query beforeAggregateQuery, AggregationDefinition aggregationDefinition, Query afterAggregateQuery) {
        return aggregator(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery).list();
    }

    default List<Map<String, Object>> aggregate(AggregationDefinition aggregationDefinition, Query afterAggregateQuery) {
        return aggregator(aggregationDefinition, afterAggregateQuery).list();
    }

    default List<Map<String, Object>> aggregate(Query beforeAggregateQuery, AggregationDefinition aggregationDefinition) {
        return aggregator(beforeAggregateQuery, aggregationDefinition).list();
    }

    default List<Map<String, Object>> aggregate(AggregationDefinition aggregationDefinition) {
        return aggregator(aggregationDefinition).list();
    }

}

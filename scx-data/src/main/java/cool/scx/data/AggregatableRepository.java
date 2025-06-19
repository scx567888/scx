package cool.scx.data;

import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.exception.DataAccessException;
import cool.scx.data.query.Query;

import java.util.List;
import java.util.Map;

import static cool.scx.data.query.QueryBuilder.query;

/// 拥有聚合能力的 Repository
///
/// @param <Entity> Entity
/// @param <ID>     ID
/// @author scx567888
/// @version 0.0.1
public interface AggregatableRepository<Entity, ID, C extends DataContext> extends Repository<Entity, ID, C> {

    /// 创建一个数据聚合器
    ///
    /// @param beforeAggregateQuery 聚合前 Query
    /// @param aggregation          聚合定义
    /// @param afterAggregateQuery  聚合后 Query
    /// @return 聚合器
    Aggregator aggregator(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery);

    Aggregator aggregator(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery, C dataContext);

    default Aggregator aggregator(Aggregation aggregation, Query afterAggregateQuery) {
        return aggregator(query(), aggregation, afterAggregateQuery);
    }

    default Aggregator aggregator(Aggregation aggregation, Query afterAggregateQuery, C dataContext) {
        return aggregator(query(), aggregation, afterAggregateQuery, dataContext);
    }

    default Aggregator aggregator(Query beforeAggregateQuery, Aggregation aggregation) {
        return aggregator(beforeAggregateQuery, aggregation, query());
    }

    default Aggregator aggregator(Query beforeAggregateQuery, Aggregation aggregation, C dataContext) {
        return aggregator(beforeAggregateQuery, aggregation, query(), dataContext);
    }

    default Aggregator aggregator(Aggregation aggregation) {
        return aggregator(query(), aggregation, query());
    }

    default Aggregator aggregator(Aggregation aggregation, C dataContext) {
        return aggregator(query(), aggregation, query(), dataContext);
    }

    default List<Map<String, Object>> aggregate(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation, afterAggregateQuery).list();
    }

    default List<Map<String, Object>> aggregate(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery, C dataContext) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation, afterAggregateQuery, dataContext).list();
    }

    default List<Map<String, Object>> aggregate(Aggregation aggregation, Query afterAggregateQuery) throws DataAccessException {
        return aggregator(aggregation, afterAggregateQuery).list();
    }

    default List<Map<String, Object>> aggregate(Aggregation aggregation, Query afterAggregateQuery, C dataContext) throws DataAccessException {
        return aggregator(aggregation, afterAggregateQuery, dataContext).list();
    }

    default List<Map<String, Object>> aggregate(Query beforeAggregateQuery, Aggregation aggregation) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation).list();
    }

    default List<Map<String, Object>> aggregate(Query beforeAggregateQuery, Aggregation aggregation, C dataContext) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation, dataContext).list();
    }

    default List<Map<String, Object>> aggregate(Aggregation aggregation) throws DataAccessException {
        return aggregator(aggregation).list();
    }

    default List<Map<String, Object>> aggregate(Aggregation aggregation, C dataContext) throws DataAccessException {
        return aggregator(aggregation, dataContext).list();
    }

    default Map<String, Object> aggregateFirst(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation, afterAggregateQuery).first();
    }

    default Map<String, Object> aggregateFirst(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery, C dataContext) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation, afterAggregateQuery, dataContext).first();
    }

    default Map<String, Object> aggregateFirst(Aggregation aggregation, Query afterAggregateQuery) throws DataAccessException {
        return aggregator(aggregation, afterAggregateQuery).first();
    }

    default Map<String, Object> aggregateFirst(Aggregation aggregation, Query afterAggregateQuery, C dataContext) throws DataAccessException {
        return aggregator(aggregation, afterAggregateQuery, dataContext).first();
    }

    default Map<String, Object> aggregateFirst(Query beforeAggregateQuery, Aggregation aggregation) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation).first();
    }

    default Map<String, Object> aggregateFirst(Query beforeAggregateQuery, Aggregation aggregation, C dataContext) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation, dataContext).first();
    }

    default Map<String, Object> aggregateFirst(Aggregation aggregation) throws DataAccessException {
        return aggregator(aggregation).first();
    }

    default Map<String, Object> aggregateFirst(Aggregation aggregation, C dataContext) throws DataAccessException {
        return aggregator(aggregation, dataContext).first();
    }

}

package cool.scx.data;

import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.exception.DataAccessException;
import cool.scx.data.query.Query;

import java.util.List;
import java.util.Map;

import static cool.scx.data.query.QueryBuilder.query;

/// 拥有聚合能力和事务的 Repository
///
/// @param <Entity> Entity
/// @param <ID>     ID
/// @author scx567888
/// @version 0.0.1
public interface TransactionalAggregatableRepository<Entity, ID, T extends TransactionContext> extends TransactionalRepository<Entity, ID, T>, AggregatableRepository<Entity, ID> {

    Aggregator aggregator(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery, T transactionContext);

    default Aggregator aggregator(Aggregation aggregation, Query afterAggregateQuery, T transactionContext) {
        return aggregator(query(), aggregation, afterAggregateQuery, transactionContext);
    }

    default Aggregator aggregator(Query beforeAggregateQuery, Aggregation aggregation, T transactionContext) {
        return aggregator(beforeAggregateQuery, aggregation, query(), transactionContext);
    }

    default Aggregator aggregator(Aggregation aggregation, T transactionContext) {
        return aggregator(query(), aggregation, query(), transactionContext);
    }

    default List<Map<String, Object>> aggregate(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery, T transactionContext) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation, afterAggregateQuery, transactionContext).list();
    }

    default List<Map<String, Object>> aggregate(Aggregation aggregation, Query afterAggregateQuery, T transactionContext) throws DataAccessException {
        return aggregator(aggregation, afterAggregateQuery, transactionContext).list();
    }

    default List<Map<String, Object>> aggregate(Query beforeAggregateQuery, Aggregation aggregation, T transactionContext) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation, transactionContext).list();
    }

    default List<Map<String, Object>> aggregate(Aggregation aggregation, T transactionContext) throws DataAccessException {
        return aggregator(aggregation, transactionContext).list();
    }

    default Map<String, Object> aggregateFirst(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery, T transactionContext) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation, afterAggregateQuery, transactionContext).first();
    }

    default Map<String, Object> aggregateFirst(Aggregation aggregation, Query afterAggregateQuery, T transactionContext) throws DataAccessException {
        return aggregator(aggregation, afterAggregateQuery, transactionContext).first();
    }

    default Map<String, Object> aggregateFirst(Query beforeAggregateQuery, Aggregation aggregation, T transactionContext) throws DataAccessException {
        return aggregator(beforeAggregateQuery, aggregation, transactionContext).first();
    }

    default Map<String, Object> aggregateFirst(Aggregation aggregation, T transactionContext) throws DataAccessException {
        return aggregator(aggregation, transactionContext).first();
    }

}

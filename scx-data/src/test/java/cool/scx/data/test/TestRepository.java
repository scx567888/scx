package cool.scx.data.test;

import cool.scx.data.*;
import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.exception.DataAccessException;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;

import java.util.Collection;
import java.util.List;

public class TestRepository<Entity> implements AggregatableRepository<Entity, Long, TestTransactionContext>, LockableRepository<Entity, Long, TestTransactionContext> {

    @Override
    public Aggregator aggregator(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery) {
        return null;
    }

    @Override
    public Aggregator aggregator(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery, TestTransactionContext dataContext) {
        return null;
    }

    @Override
    public Finder<Entity> finder(Query query, FieldPolicy fieldPolicy, LockMode lockMode) {
        return new TestFinder<>();
    }

    @Override
    public Finder<Entity> finder(Query query, FieldPolicy fieldPolicy, LockMode lockMode, TestTransactionContext dataContext) {
        return new TestFinder<>();
    }

    @Override
    public Long add(Entity entity, FieldPolicy fieldPolicy) throws DataAccessException {
        return null;
    }

    @Override
    public List<Long> add(Collection<Entity> entityList, FieldPolicy fieldPolicy) throws DataAccessException {
        return List.of();
    }

    @Override
    public Finder<Entity> finder(Query query, FieldPolicy fieldPolicy) {
        return new TestFinder<>();
    }

    @Override
    public long update(Entity entity, FieldPolicy fieldPolicy, Query query) throws DataAccessException {
        return 0;
    }

    @Override
    public long delete(Query query) throws DataAccessException {
        return 0;
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void clear(TestTransactionContext dataContext) throws DataAccessException {

    }

    @Override
    public long delete(Query query, TestTransactionContext dataContext) throws DataAccessException {
        return 0;
    }

    @Override
    public long update(Entity entity, FieldPolicy fieldPolicy, Query query, TestTransactionContext dataContext) throws DataAccessException {
        return 0;
    }

    @Override
    public Finder<Entity> finder(Query query, FieldPolicy fieldPolicy, TestTransactionContext dataContext) {
        return new TestFinder<>();
    }

    @Override
    public List<Long> add(Collection<Entity> entityList, FieldPolicy fieldPolicy, TestTransactionContext dataContext) throws DataAccessException {
        return List.of();
    }

    @Override
    public Long add(Entity entity, FieldPolicy fieldPolicy, TestTransactionContext dataContext) throws DataAccessException {
        return null;
    }

}

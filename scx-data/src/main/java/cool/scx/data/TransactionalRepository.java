package cool.scx.data;

import cool.scx.data.exception.DataAccessException;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;

import java.util.Collection;
import java.util.List;

import static cool.scx.data.field_policy.FieldPolicyBuilder.includeAll;
import static cool.scx.data.query.QueryBuilder.query;

/// 支持事务版本的 Repository
public interface TransactionalRepository<Entity, ID, T extends TransactionContext> extends Repository<Entity, ID> {

    ID add(Entity entity, FieldPolicy fieldPolicy, T transactionContext) throws DataAccessException;

    List<ID> add(Collection<Entity> entityList, FieldPolicy fieldPolicy, T transactionContext) throws DataAccessException;

    Finder<Entity> finder(Query query, FieldPolicy fieldPolicy, T transactionContext);

    long update(Entity entity, FieldPolicy fieldPolicy, Query query, T transactionContext) throws DataAccessException;

    long delete(Query query, T transactionContext) throws DataAccessException;

    void clear(T transactionContext) throws DataAccessException;

    default ID add(Entity entity, T transactionContext) throws DataAccessException {
        return add(entity, includeAll(), transactionContext);
    }

    default ID add(FieldPolicy fieldPolicy, T transactionContext) throws DataAccessException {
        return add((Entity) null, fieldPolicy, transactionContext);
    }

    default List<ID> add(Collection<Entity> entityList, T transactionContext) throws DataAccessException {
        return add(entityList, includeAll(), transactionContext);
    }

    default Finder<Entity> finder(Query query, T transactionContext) {
        return finder(query, includeAll(), transactionContext);
    }

    default Finder<Entity> finder(FieldPolicy fieldPolicy, T transactionContext) {
        return finder(query(), fieldPolicy, transactionContext);
    }

    default Finder<Entity> finder(T transactionContext) {
        return finder(query(), includeAll(), transactionContext);
    }

    default List<Entity> find(Query query, FieldPolicy fieldPolicy, T transactionContext) throws DataAccessException {
        return finder(query, fieldPolicy, transactionContext).list();
    }

    default List<Entity> find(Query query, T transactionContext) throws DataAccessException {
        return finder(query, transactionContext).list();
    }

    default List<Entity> find(FieldPolicy fieldPolicy, T transactionContext) throws DataAccessException {
        return finder(fieldPolicy, transactionContext).list();
    }

    default List<Entity> find(T transactionContext) throws DataAccessException {
        return finder(transactionContext).list();
    }

    default Entity findFirst(Query query, FieldPolicy fieldPolicy, T transactionContext) throws DataAccessException {
        return finder(query, fieldPolicy, transactionContext).first();
    }

    default Entity findFirst(Query query, T transactionContext) throws DataAccessException {
        return finder(query, transactionContext).first();
    }

    default long update(Entity entity, Query query, T transactionContext) throws DataAccessException {
        return update(entity, includeAll(), query, transactionContext);
    }

    default long update(FieldPolicy fieldPolicy, Query query, T transactionContext) throws DataAccessException {
        return update(null, fieldPolicy, query, transactionContext);
    }

    default long count(Query query, T transactionContext) throws DataAccessException {
        return finder(query, transactionContext).count();
    }

    default long count(T transactionContext) throws DataAccessException {
        return finder(transactionContext).count();
    }

}

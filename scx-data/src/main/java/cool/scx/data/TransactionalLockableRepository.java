package cool.scx.data;

import cool.scx.data.exception.DataAccessException;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;

import java.util.List;

import static cool.scx.data.field_policy.FieldPolicyBuilder.includeAll;
import static cool.scx.data.query.QueryBuilder.query;

public interface TransactionalLockableRepository<Entity, ID, T extends TransactionContext> extends TransactionalRepository<Entity, ID, T>, LockableRepository<Entity, ID> {

    Finder<Entity> finder(Query query, FieldPolicy fieldPolicy, LockMode lockMode, T transactionContext);

    default Finder<Entity> finder(Query query, LockMode lockMode, T transactionContext) {
        return finder(query, includeAll(), lockMode, transactionContext);
    }

    default Finder<Entity> finder(FieldPolicy fieldPolicy, LockMode lockMode, T transactionContext) {
        return finder(query(), fieldPolicy, lockMode, transactionContext);
    }

    default Finder<Entity> finder(LockMode lockMode, T transactionContext) {
        return finder(query(), includeAll(), lockMode, transactionContext);
    }

    default List<Entity> find(Query query, FieldPolicy fieldPolicy, LockMode lockMode, T transactionContext) throws DataAccessException {
        return finder(query, fieldPolicy, lockMode, transactionContext).list();
    }

    default List<Entity> find(Query query, LockMode lockMode, T transactionContext) throws DataAccessException {
        return finder(query, lockMode, transactionContext).list();
    }

    default List<Entity> find(FieldPolicy fieldPolicy, LockMode lockMode, T transactionContext) throws DataAccessException {
        return finder(fieldPolicy, lockMode, transactionContext).list();
    }

    default List<Entity> find(LockMode lockMode, T transactionContext) throws DataAccessException {
        return finder(lockMode, transactionContext).list();
    }

    default Entity findFirst(Query query, FieldPolicy fieldPolicy, LockMode lockMode, T transactionContext) throws DataAccessException {
        return finder(query, fieldPolicy, lockMode, transactionContext).first();
    }

    default Entity findFirst(Query query, LockMode lockMode, T transactionContext) throws DataAccessException {
        return finder(query, lockMode, transactionContext).first();
    }

}

package cool.scx.data.jdbc;

import cool.scx.data.Finder;
import cool.scx.data.LockMode;
import cool.scx.data.exception.DataAccessException;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;
import cool.scx.function.ConsumerX;
import cool.scx.jdbc.sql.SQLRunnerException;

import java.util.List;
import java.util.Map;

import static cool.scx.jdbc.result_handler.ResultHandler.*;

public class JDBCFinder<Entity> implements Finder<Entity> {

    private final JDBCRepository<Entity> repository;
    private final Query query;
    private final FieldPolicy fieldPolicy;
    private final LockMode lockMode;

    public JDBCFinder(JDBCRepository<Entity> repository, Query query, FieldPolicy fieldPolicy) {
        this(repository, query, fieldPolicy, null);
    }

    public JDBCFinder(JDBCRepository<Entity> repository, Query query, FieldPolicy fieldPolicy, LockMode lockMode) {
        this.repository = repository;
        this.query = query;
        this.fieldPolicy = fieldPolicy;
        this.lockMode = lockMode;
    }

    @Override
    public List<Entity> list() throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), repository.entityBeanListHandler);
        } catch (SQLRunnerException e) {
            // SQLRunnerException 本身就是包装层, 没必要二次包装 这里直接提取原始异常
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <T> List<T> list(Class<T> resultType) throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), ofBeanList(resultType, repository.beanColumnNameMapping));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public List<Map<String, Object>> listMap() throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), ofMapList(repository.mapBuilder));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <E extends Throwable> void forEach(ConsumerX<Entity, E> entityConsumer) throws DataAccessException, E {
        try {
            repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), ofBeanConsumer(repository.beanBuilder, entityConsumer));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <T, E extends Throwable> void forEach(ConsumerX<T, E> entityConsumer, Class<T> resultType) throws DataAccessException, E {
        try {
            repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), ofBeanConsumer(resultType, repository.beanColumnNameMapping, entityConsumer));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <E extends Throwable> void forEachMap(ConsumerX<Map<String, Object>, E> entityConsumer) throws DataAccessException, E {
        try {
            repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), ofMapConsumer(repository.mapBuilder, entityConsumer));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public Entity first() throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildSelectFirstSQL(query, fieldPolicy, lockMode), repository.entityBeanHandler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <T> T first(Class<T> resultType) throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildSelectFirstSQL(query, fieldPolicy, lockMode), ofBean(resultType, repository.beanColumnNameMapping));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public Map<String, Object> firstMap() throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildSelectFirstSQL(query, fieldPolicy, lockMode), ofMap(repository.mapBuilder));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public long count() throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildCountSQL(query), repository.countResultHandler);
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

}

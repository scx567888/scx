package cool.scx.data.jdbc;

import cool.scx.data.Finder;
import cool.scx.data.LockMode;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
    public List<Entity> list() {
        return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), repository.entityBeanListHandler);
    }

    @Override
    public <T> List<T> list(Class<T> resultType) {
        return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), ofBeanList(resultType, repository.beanColumnNameMapping));
    }

    @Override
    public List<Map<String, Object>> listMap() {
        return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), ofMapList(repository.mapBuilder));
    }

    @Override
    public void forEach(Consumer<Entity> entityConsumer) {
        repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), ofBeanConsumer(repository.beanBuilder, entityConsumer));
    }

    @Override
    public <T> void forEach(Consumer<T> entityConsumer, Class<T> resultType) {
        repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), ofBeanConsumer(resultType, repository.beanColumnNameMapping, entityConsumer));
    }

    @Override
    public void forEachMap(Consumer<Map<String, Object>> entityConsumer) {
        repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy, lockMode), ofMapConsumer(repository.mapBuilder, entityConsumer));
    }

    @Override
    public Entity first() {
        return repository.sqlRunner.query(repository.buildSelectFirstSQL(query, fieldPolicy, lockMode), repository.entityBeanHandler);
    }

    @Override
    public <T> T first(Class<T> resultType) {
        return repository.sqlRunner.query(repository.buildSelectFirstSQL(query, fieldPolicy, lockMode), ofBean(resultType, repository.beanColumnNameMapping));
    }

    @Override
    public Map<String, Object> firstMap() {
        return repository.sqlRunner.query(repository.buildSelectFirstSQL(query, fieldPolicy, lockMode), ofMap(repository.mapBuilder));
    }

    @Override
    public long count() {
        return repository.sqlRunner.query(repository.buildCountSQL(query), repository.countResultHandler);
    }

}

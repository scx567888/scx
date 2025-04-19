package cool.scx.data.jdbc;

import cool.scx.data.FindExecutor;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static cool.scx.jdbc.result_handler.ResultHandler.*;

public class JDBCFindExecutor<Entity> implements FindExecutor<Entity> {

    private final JDBCRepository<Entity> repository;
    private final Query query;
    private final FieldPolicy fieldPolicy;

    public JDBCFindExecutor(JDBCRepository<Entity> repository, Query query, FieldPolicy fieldPolicy) {
        this.repository = repository;
        this.query = query;
        this.fieldPolicy = fieldPolicy;
    }

    @Override
    public List<Entity> list() {
        return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy), repository.entityBeanListHandler);
    }

    @Override
    public <T> List<T> list(Class<T> resultType) {
        return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy), ofBeanList(resultType, repository.columnNameMapping));
    }

    @Override
    public List<Map<String, Object>> mapList() {
        return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy), ofMapList());
    }

    @Override
    public void forEach(Consumer<Entity> entityConsumer) {
        repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy), ofBeanConsumer(repository.beanBuilder, entityConsumer));
    }

    @Override
    public <T> void forEach(Consumer<T> entityConsumer, Class<T> resultType) {
        repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy), ofBeanConsumer(resultType, repository.columnNameMapping, entityConsumer));
    }

    @Override
    public void forEachMap(Consumer<Map<String, Object>> entityConsumer) {
        repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy), ofMapConsumer(entityConsumer));
    }

    @Override
    public Entity first() {
        return repository.sqlRunner.query(repository.buildGetSQL(query, fieldPolicy), repository.entityBeanHandler);
    }

    @Override
    public <T> T first(Class<T> resultType) {
        return repository.sqlRunner.query(repository.buildGetSQL(query, fieldPolicy), ofBean(resultType, repository.columnNameMapping));
    }

    @Override
    public Map<String, Object> firstMap() {
        return repository.sqlRunner.query(repository.buildGetSQL(query, fieldPolicy), ofMap());
    }

    @Override
    public long count() {
        return repository.sqlRunner.query(repository.buildCountSQL(query), repository.countResultHandler);
    }

}

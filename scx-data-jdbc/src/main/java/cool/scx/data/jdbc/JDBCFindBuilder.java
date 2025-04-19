package cool.scx.data.jdbc;

import cool.scx.data.FindBuilder;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;
import cool.scx.data.query.QueryBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static cool.scx.data.field_policy.FieldPolicyBuilder.includedAll;
import static cool.scx.jdbc.result_handler.ResultHandler.*;

public class JDBCFindBuilder<Entity> implements FindBuilder<Entity> {

    private final JDBCRepository<Entity> repository;
    private Query query;
    private FieldPolicy fieldPolicy;

    public JDBCFindBuilder(JDBCRepository<Entity> repository) {
        this.repository = repository;
        this.query = QueryBuilder.query();
        this.fieldPolicy = includedAll();
    }

    @Override
    public FindBuilder<Entity> query(Query query) {
        this.query = query;
        return this;
    }

    @Override
    public FindBuilder<Entity> fieldPolicy(FieldPolicy fieldPolicy) {
        this.fieldPolicy = fieldPolicy;
        return this;
    }

    @Override
    public List<Entity> toList() {
        return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy), repository.entityBeanListHandler);
    }

    @Override
    public <T> List<T> toList(Class<T> resultType) {
        return repository.sqlRunner.query(repository.buildSelectSQL(query, fieldPolicy), ofBeanList(resultType, repository.columnNameMapping));
    }

    @Override
    public List<Map<String, Object>> toMapList() {
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
    public Entity getFirst() {
        return repository.sqlRunner.query(repository.buildGetSQL(query, fieldPolicy), repository.entityBeanHandler);
    }

    @Override
    public <T> T getFirst(Class<T> resultType) {
        return repository.sqlRunner.query(repository.buildGetSQL(query, fieldPolicy), ofBean(resultType, repository.columnNameMapping));
    }

    @Override
    public Map<String, Object> getFirstMap() {
        return repository.sqlRunner.query(repository.buildGetSQL(query, fieldPolicy), ofMap());
    }

    @Override
    public long count() {
        return repository.sqlRunner.query(repository.buildCountSQL(query), repository.countResultHandler);
    }

}

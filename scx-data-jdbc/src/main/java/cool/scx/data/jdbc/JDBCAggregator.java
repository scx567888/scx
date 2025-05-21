package cool.scx.data.jdbc;

import cool.scx.data.Aggregator;
import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.query.Query;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static cool.scx.jdbc.result_handler.ResultHandler.*;

public class JDBCAggregator implements Aggregator {

    private final JDBCRepository<?> repository;
    private final Query beforeAggregateQuery;
    private final Aggregation aggregationDefinition;
    private final Query afterAggregateQuery;

    public JDBCAggregator(JDBCRepository<?> repository, Query beforeAggregateQuery, Aggregation aggregationDefinition, Query afterAggregateQuery) {
        this.repository = repository;
        this.beforeAggregateQuery = beforeAggregateQuery;
        this.aggregationDefinition = aggregationDefinition;
        this.afterAggregateQuery = afterAggregateQuery;
    }

    @Override
    public <T> List<T> list(Class<T> resultType) {
        return repository.sqlRunner.query(repository.buildAggregateSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofBeanList(resultType, repository.beanColumnNameMapping));
    }

    @Override
    public List<Map<String, Object>> list() {
        return repository.sqlRunner.query(repository.buildAggregateSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofMapList(repository.mapBuilder));
    }

    @Override
    public <T> void forEach(Consumer<T> resultConsumer, Class<T> resultType) {
        repository.sqlRunner.query(repository.buildAggregateSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofBeanConsumer(resultType, repository.beanColumnNameMapping, resultConsumer));
    }

    @Override
    public void forEach(Consumer<Map<String, Object>> resultConsumer) {
        repository.sqlRunner.query(repository.buildAggregateSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofMapConsumer(repository.mapBuilder, resultConsumer));
    }

    @Override
    public <T> T first(Class<T> resultType) {
        return repository.sqlRunner.query(repository.buildAggregateFirstSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofBean(resultType, repository.beanColumnNameMapping));
    }

    @Override
    public Map<String, Object> first() {
        return repository.sqlRunner.query(repository.buildAggregateFirstSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofMap(repository.mapBuilder));
    }

}

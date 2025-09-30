package cool.scx.data.jdbc;

import cool.scx.data.Aggregator;
import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.exception.DataAccessException;
import cool.scx.data.query.Query;
import cool.scx.function.Function1Void;
import cool.scx.jdbc.sql.SQLRunnerException;

import java.util.List;
import java.util.Map;

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
    public <T> List<T> list(Class<T> resultType) throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildAggregateSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofBeanList(resultType, repository.beanColumnNameMapping));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public List<Map<String, Object>> list() throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildAggregateSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofMapList(repository.mapBuilder));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <T, X extends Throwable> void forEach(Function1Void<T, X> resultConsumer, Class<T> resultType) throws DataAccessException, X {
        try {
            repository.sqlRunner.query(repository.buildAggregateSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofBeanConsumer(resultType, repository.beanColumnNameMapping, resultConsumer));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <X extends Throwable> void forEach(Function1Void<Map<String, Object>, X> resultConsumer) throws DataAccessException, X {
        try {
            repository.sqlRunner.query(repository.buildAggregateSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofMapConsumer(repository.mapBuilder, resultConsumer));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public <T> T first(Class<T> resultType) throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildAggregateFirstSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofBean(resultType, repository.beanColumnNameMapping));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public Map<String, Object> first() throws DataAccessException {
        try {
            return repository.sqlRunner.query(repository.buildAggregateFirstSQL(beforeAggregateQuery, aggregationDefinition, afterAggregateQuery), ofMap(repository.mapBuilder));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

}

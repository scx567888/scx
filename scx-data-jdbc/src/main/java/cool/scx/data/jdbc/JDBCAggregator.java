package cool.scx.data.jdbc;

import cool.scx.data.Aggregator;
import cool.scx.data.aggregation_definition.AggregationDefinition;
import cool.scx.data.query.Query;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

//todo 待完成
public class JDBCAggregator implements Aggregator {

    private final JDBCRepository<?> repository;
    private final Query beforeAggregateQuery;
    private final AggregationDefinition aggregationDefinition;
    private final Query afterAggregateQuery;

    public JDBCAggregator(JDBCRepository<?> repository, Query beforeAggregateQuery, AggregationDefinition aggregationDefinition, Query afterAggregateQuery) {
        this.repository = repository;
        this.beforeAggregateQuery = beforeAggregateQuery;
        this.aggregationDefinition = aggregationDefinition;
        this.afterAggregateQuery = afterAggregateQuery;
    }

    @Override
    public <T> List<T> list(Class<T> resultType) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> list() {
        return List.of();
    }

    @Override
    public <T> void forEach(Consumer<T> resultConsumer, Class<T> resultType) {

    }

    @Override
    public void forEach(Consumer<Map<String, Object>> resultConsumer) {

    }

    @Override
    public <T> T first(Class<T> resultType) {
        return null;
    }

    @Override
    public Map<String, Object> first() {
        return Map.of();
    }

}

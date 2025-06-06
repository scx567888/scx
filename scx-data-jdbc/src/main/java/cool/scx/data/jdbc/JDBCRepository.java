package cool.scx.data.jdbc;

import cool.scx.data.*;
import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.exception.DataAccessException;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.column_name_mapping.BeanColumnNameMapping;
import cool.scx.data.jdbc.column_name_mapping.MapFieldNameMapping;
import cool.scx.data.jdbc.mapping.AnnotationConfigTable;
import cool.scx.data.jdbc.parser.JDBCColumnNameParser;
import cool.scx.data.jdbc.parser.JDBCGroupByParser;
import cool.scx.data.jdbc.parser.JDBCOrderByParser;
import cool.scx.data.jdbc.parser.JDBCWhereParser;
import cool.scx.data.jdbc.sql_builder.*;
import cool.scx.data.query.Query;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.result_handler.ResultHandler;
import cool.scx.jdbc.result_handler.bean_builder.BeanBuilder;
import cool.scx.jdbc.result_handler.map_builder.MapBuilder;
import cool.scx.jdbc.sql.SQL;
import cool.scx.jdbc.sql.SQLRunner;
import cool.scx.jdbc.sql.SQLRunnerException;

import java.util.Collection;
import java.util.List;

import static cool.scx.jdbc.result_handler.ResultHandler.*;
import static cool.scx.jdbc.sql.SQL.sql;

/// 使用 JDBC 接口, 通过 SQL 操作关系型数据库的 DAO
///
/// @author scx567888
/// @version 0.0.1
public class JDBCRepository<Entity> implements AggregatableRepository<Entity, Long>, LockableRepository<Entity, Long> {

    // *********** 基本字段 ***************
    final Class<Entity> entityClass;
    final JDBCContext jdbcContext;
    final AnnotationConfigTable table;
    final SQLRunner sqlRunner;

    // *********** 结果解析器 ***************
    final BeanColumnNameMapping beanColumnNameMapping;
    final MapFieldNameMapping mapFieldNameMapping;
    final BeanBuilder<Entity> beanBuilder;
    final MapBuilder mapBuilder;
    final ResultHandler<List<Entity>> entityBeanListHandler;
    final ResultHandler<Entity> entityBeanHandler;
    final ResultHandler<Long> countResultHandler;

    // *********** SQL 语句构造器 ***************
    final InsertSQLBuilder insertSQLBuilder;
    final SelectSQLBuilder selectSQLBuilder;
    final UpdateSQLBuilder updateSQLBuilder;
    final DeleteSQLBuilder deleteSQLBuilder;
    final CountSQLBuilder countSQLBuilder;
    final AggregateSQLBuilder aggregateSQLBuilder;

    public JDBCRepository(Class<Entity> entityClass, JDBCContext jdbcContext) {
        //1, 初始化基本字段
        this.entityClass = entityClass;
        this.jdbcContext = jdbcContext;
        this.table = new AnnotationConfigTable(entityClass);
        this.sqlRunner = jdbcContext.sqlRunner();

        //2, 创建返回值解析器
        this.beanColumnNameMapping = new BeanColumnNameMapping(table);
        this.mapFieldNameMapping = new MapFieldNameMapping(table);
        this.beanBuilder = BeanBuilder.of(this.entityClass, beanColumnNameMapping);
        this.mapBuilder = MapBuilder.of(mapFieldNameMapping);
        this.entityBeanListHandler = ofBeanList(beanBuilder);
        this.entityBeanHandler = ofBean(beanBuilder);
        this.countResultHandler = ofSingleValue("count", Long.class);

        //3, 创建 SQL 语句构造器
        var dialect = jdbcContext.dialect();
        var columnNameParser = new JDBCColumnNameParser(table, dialect);
        var whereParser = new JDBCWhereParser(columnNameParser, dialect);
        var groupByParser = new JDBCGroupByParser(columnNameParser);
        var orderByParser = new JDBCOrderByParser(columnNameParser);
        this.insertSQLBuilder = new InsertSQLBuilder(table, dialect, columnNameParser);
        this.selectSQLBuilder = new SelectSQLBuilder(table, dialect, whereParser, orderByParser);
        this.updateSQLBuilder = new UpdateSQLBuilder(table, dialect, columnNameParser, whereParser, orderByParser);
        this.deleteSQLBuilder = new DeleteSQLBuilder(table, dialect, whereParser, orderByParser);
        this.countSQLBuilder = new CountSQLBuilder(table, dialect, whereParser);
        this.aggregateSQLBuilder = new AggregateSQLBuilder(table, dialect, whereParser, groupByParser, orderByParser);
    }

    @Override
    public final Long add(Entity entity, FieldPolicy fieldPolicy) throws DataAccessException {
        try {
            return sqlRunner.update(buildInsertSQL(entity, fieldPolicy)).firstGeneratedKey();
        } catch (SQLRunnerException e) {
            // SQLRunnerException 本身就是包装层, 没必要二次包装 这里直接提取原始异常
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public final List<Long> add(Collection<Entity> entityList, FieldPolicy fieldPolicy) throws DataAccessException {
        try {
            return sqlRunner.updateBatch(buildInsertBatchSQL(entityList, fieldPolicy)).generatedKeys();
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public final Finder<Entity> finder(Query query, FieldPolicy fieldPolicy, LockMode lockMode) {
        return new JDBCFinder<>(this, query, fieldPolicy, lockMode);
    }

    @Override
    public final Finder<Entity> finder(Query query, FieldPolicy fieldPolicy) {
        return new JDBCFinder<>(this, query, fieldPolicy);
    }

    @Override
    public final long update(Entity entity, FieldPolicy fieldPolicy, Query query) throws DataAccessException {
        try {
            return sqlRunner.update(buildUpdateSQL(entity, fieldPolicy, query)).affectedItemsCount();
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public final long delete(Query query) throws DataAccessException {
        try {
            return sqlRunner.update(buildDeleteSQL(query)).affectedItemsCount();
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public final void clear() throws DataAccessException {
        try {
            sqlRunner.execute(sql("truncate " + table.name()));
        } catch (SQLRunnerException e) {
            throw new DataAccessException(e.getCause());
        }
    }

    @Override
    public final Aggregator aggregator(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery) {
        return new JDBCAggregator(this, beforeAggregateQuery, aggregation, afterAggregateQuery);
    }

    public final Class<Entity> entityClass() {
        return entityClass;
    }

    public final AnnotationConfigTable table() {
        return table;
    }

    public final SQLRunner sqlRunner() {
        return sqlRunner;
    }

    public BeanBuilder<Entity> beanBuilder() {
        return beanBuilder;
    }

    public ResultHandler<List<Entity>> entityBeanListHandler() {
        return entityBeanListHandler;
    }

    public ResultHandler<Entity> entityBeanHandler() {
        return entityBeanHandler;
    }

    public JDBCContext jdbcContext() {
        return jdbcContext;
    }

    public SQL buildInsertSQL(Entity entity, FieldPolicy fieldPolicy) {
        return insertSQLBuilder.buildInsertSQL(entity, fieldPolicy);
    }

    public SQL buildInsertBatchSQL(Collection<? extends Entity> entityList, FieldPolicy fieldPolicy) {
        return insertSQLBuilder.buildInsertBatchSQL(entityList, fieldPolicy);
    }

    public SQL buildSelectSQL(Query query, FieldPolicy fieldPolicy) {
        return selectSQLBuilder.buildSelectSQL(query, fieldPolicy);
    }

    public SQL buildSelectFirstSQL(Query query, FieldPolicy fieldPolicy) {
        return selectSQLBuilder.buildSelectFirstSQL(query, fieldPolicy);
    }

    public SQL buildUpdateSQL(Entity entity, FieldPolicy fieldPolicy, Query query) {
        return updateSQLBuilder.buildUpdateSQL(entity, fieldPolicy, query);
    }

    public SQL buildDeleteSQL(Query query) {
        return deleteSQLBuilder.buildDeleteSQL(query);
    }

    public SQL buildCountSQL(Query query) {
        return countSQLBuilder.buildCountSQL(query);
    }

    public SQL buildSelectFirstSQLWithAlias(Query query, FieldPolicy fieldPolicy) {
        return selectSQLBuilder.buildSelectFirstSQLWithAlias(query, fieldPolicy);
    }

    public SQL buildSelectSQLWithAlias(Query query, FieldPolicy fieldPolicy) {
        return selectSQLBuilder.buildSelectSQLWithAlias(query, fieldPolicy);
    }

    public SQL buildAggregateSQL(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery) {
        return aggregateSQLBuilder.buildAggregateSQL(beforeAggregateQuery, aggregation, afterAggregateQuery);
    }

    public SQL buildAggregateFirstSQL(Query beforeAggregateQuery, Aggregation aggregation, Query afterAggregateQuery) {
        return aggregateSQLBuilder.buildAggregateFirstSQL(beforeAggregateQuery, aggregation, afterAggregateQuery);
    }

    public SQL buildSelectSQL(Query query, FieldPolicy fieldPolicy, LockMode lockMode) {
        return selectSQLBuilder.buildSelectSQL(query, fieldPolicy, lockMode);
    }

    public SQL buildSelectFirstSQL(Query query, FieldPolicy fieldPolicy, LockMode lockMode) {
        return selectSQLBuilder.buildSelectFirstSQL(query, fieldPolicy, lockMode);
    }

}

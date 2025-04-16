package cool.scx.data.jdbc;

import cool.scx.data.Repository;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.parser.JDBCDaoColumnNameParser;
import cool.scx.data.jdbc.parser.JDBCDaoGroupByParser;
import cool.scx.data.jdbc.parser.JDBCDaoOrderByParser;
import cool.scx.data.jdbc.parser.JDBCDaoWhereParser;
import cool.scx.data.jdbc.sql_builder.*;
import cool.scx.data.query.Query;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.ResultHandler;
import cool.scx.jdbc.result_handler.bean_builder.BeanBuilder;
import cool.scx.jdbc.sql.SQL;
import cool.scx.jdbc.sql.SQLRunner;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static cool.scx.common.util.ArrayUtils.tryConcat;
import static cool.scx.common.util.RandomUtils.randomString;
import static cool.scx.data.jdbc.A.filterByFieldPolicy;
import static cool.scx.data.jdbc.DataJDBCHelper.*;
import static cool.scx.jdbc.result_handler.ResultHandler.*;
import static cool.scx.jdbc.sql.SQL.sql;
import static cool.scx.jdbc.sql.SQLBuilder.*;

/// 使用 JDBC 接口, 通过 SQL 操作关系型数据库的 DAO
///
/// @author scx567888
/// @version 0.0.1
public class JDBCEntityRepository<Entity> implements Repository<Entity, Long> {
    
    private final AnnotationConfigTable table;
    private final Class<Entity> entityClass;
    private final SQLRunner sqlRunner;
    private final Dialect dialect;
    private final BeanBuilder<Entity> beanBuilder;
    private final ResultHandler<List<Entity>> entityBeanListHandler;
    private final ResultHandler<Entity> entityBeanHandler;
    private final ResultHandler<Long> countResultHandler;
    private final JDBCDaoColumnNameParser columnNameParser;
    private final JDBCDaoWhereParser whereParser;
    private final JDBCDaoGroupByParser groupByParser;
    private final JDBCDaoOrderByParser orderByParser;
    private final JDBCContext jdbcContext;
    private final Function<Field, String> columnNameMapping;
    private final InsertSQLBuilder insertSQLBuilder;
    private final SelectSQLBuilder selectSQLBuilder;
    private final UpdateSQLBuilder updateSQLBuilder;
    private final DeleteSQLBuilder deleteSQLBuilder;
    private final CountSQLBuilder countSQLBuilder;
    
    public JDBCEntityRepository(Class<Entity> entityClass, JDBCContext jdbcContext) {
        this.entityClass = entityClass;
        this.jdbcContext = jdbcContext;
        this.sqlRunner = jdbcContext.sqlRunner();
        this.dialect = jdbcContext.dialect();
        this.table = new AnnotationConfigTable(entityClass);
        this.columnNameMapping = new FieldColumnNameMapping(this.table);
        this.beanBuilder = BeanBuilder.of(this.entityClass, this.columnNameMapping);
        this.entityBeanListHandler = ofBeanList(this.beanBuilder);
        this.entityBeanHandler = ofBean(this.beanBuilder);
        this.countResultHandler = ofSingleValue("count", Long.class);
        this.columnNameParser = new JDBCDaoColumnNameParser(table, dialect);
        this.whereParser = new JDBCDaoWhereParser(columnNameParser);
        this.groupByParser = new JDBCDaoGroupByParser(columnNameParser);
        this.orderByParser = new JDBCDaoOrderByParser(columnNameParser);
        this.insertSQLBuilder = new InsertSQLBuilder(table, dialect, columnNameParser);
        this.selectSQLBuilder = new SelectSQLBuilder(table, dialect, whereParser, groupByParser, orderByParser);
        this.updateSQLBuilder = new UpdateSQLBuilder(table, dialect, columnNameParser, whereParser, orderByParser);
        this.deleteSQLBuilder = new DeleteSQLBuilder(table, dialect,  whereParser, orderByParser);
        this.countSQLBuilder = new CountSQLBuilder(table, dialect, whereParser, groupByParser);
    }

    @Override
    public final Long add(Entity entity, FieldPolicy updateFilter) {
        return sqlRunner.update(buildInsertSQL(entity, updateFilter)).firstGeneratedKey();
    }

    @Override
    public final List<Long> add(Collection<Entity> entityList, FieldPolicy updateFilter) {
        return sqlRunner.updateBatch(buildInsertBatchSQL(entityList, updateFilter)).generatedKeys();
    }

    @Override
    public final List<Entity> find(Query query, FieldPolicy selectFilter) {
        return sqlRunner.query(buildSelectSQL(query, selectFilter), entityBeanListHandler);
    }

    @Override
    public void find(Query query, FieldPolicy fieldFilter, Consumer<Entity> consumer) {
        sqlRunner.query(buildSelectSQL(query, fieldFilter), ofBeanConsumer(beanBuilder, consumer));
    }

    public Entity get(Query query, FieldPolicy columnFilter) {
        return sqlRunner.query(buildGetSQL(query, columnFilter), entityBeanHandler);
    }

    @Override
    public final long update(Entity entity, Query query, FieldPolicy updateFilter) {
        return sqlRunner.update(buildUpdateSQL(entity, query, updateFilter)).affectedItemsCount();
    }

    @Override
    public final long delete(Query query) {
        return sqlRunner.update(buildDeleteSQL(query)).affectedItemsCount();
    }

    @Override
    public final long count(Query query) {
        return sqlRunner.query(buildCountSQL(query), countResultHandler);
    }

    @Override
    public final void clear() {
        this.sqlRunner.execute(sql("truncate " + table.name()));
    }

    @Override
    public final Class<Entity> entityClass() {
        return this.entityClass;
    }

    public final AnnotationConfigTable tableInfo() {
        return this.table;
    }

    public final SQLRunner sqlRunner() {
        return this.sqlRunner;
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

    public SQL buildGetSQL(Query query, FieldPolicy fieldPolicy) {
        return selectSQLBuilder.buildGetSQL(query, fieldPolicy);
    }

    public SQL buildUpdateSQL(Entity entity, Query query, FieldPolicy updateFilter) {
        return updateSQLBuilder.buildUpdateSQL(entity, query, updateFilter);
    }

    public SQL buildDeleteSQL(Query query) {
       return deleteSQLBuilder.buildDeleteSQL(query);
    }

    public SQL buildCountSQL(Query query) {
        return countSQLBuilder.buildCountSQL(query);
    }

    /// 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
    /// 此方法便是用于生成嵌套的 sql 的
    ///
    /// @param query        q
    /// @param selectFilter s
    /// @return a
    public SQL buildGetSQLWithAlias(Query query, FieldPolicy selectFilter) {
        var sql0 = buildGetSQL(query, selectFilter);
        var sql = Select("*")
                .From("(" + sql0.sql() + ")")
                .GetSQL(dialect);
        return sql(sql + " AS " + table.name() + "_" + randomString(6), sql0.params());
    }

    /// 在 mysql 中 不支持 in 子句中包含 limit 但是我们可以使用 一个嵌套的别名表来跳过检查
    /// 此方法便是用于生成嵌套的 sql 的
    ///
    /// @param query        q
    /// @param selectFilter s
    /// @return a
    public SQL buildSelectSQLWithAlias(Query query, FieldPolicy selectFilter) {
        var sql0 = buildSelectSQL(query, selectFilter);
        var sql = Select("*")
                .From("(" + sql0.sql() + ")")
                .GetSQL(dialect);
        return sql(sql + " AS " + table.name() + "_" + randomString(6), sql0.params());
    }

}

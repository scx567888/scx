package cool.scx.orm.xdevapi;

import com.mysql.cj.xdevapi.Schema;
import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.Table;
import cool.scx.orm.AnnotationConfigTable;
import cool.scx.orm.BaseDao;
import cool.scx.orm.ColumnFilter;
import cool.scx.orm.Query;
import cool.scx.util.StringUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static cool.scx.orm.jdbc.JDBCDao.getGroupByColumns;
import static cool.scx.orm.jdbc.JDBCDao.getOrderByClauses;

public class MySQLXTableDao<Entity> implements BaseDao<Entity, String> {

    private final Class<Entity> entityClass;
    private final Session session;
    private final Schema schema;
    private final MySQLXDaoWhereParser whereParser;
    private final Table table;
    private final AnnotationConfigTable tableInfo;

    public MySQLXTableDao(Class<Entity> entityClass, Session session) {
        this.entityClass = entityClass;
        this.session = session;
        this.tableInfo = new AnnotationConfigTable(entityClass);
        this.schema = session.getDefaultSchema();
        this.table = schema.getTable(tableInfo.name());
        this.whereParser = new MySQLXDaoWhereParser();
    }

    @Override
    public String insert(Entity entity, ColumnFilter columnFilter) {
//        var selectColumnInfos = columnFilter.filter(tableInfo);
//        var selectColumns = Arrays.stream(selectColumnInfos).map(cool.scx.orm.jdbc.mapping.Column::name).toArray(String[]::new);
//        var whereClauseAndWhereParams = whereParser.parseWhere(query.where());
//        table.select(selectColumns).where()
//        return null;
        return null;
    }

    @Override
    public List<String> insertBatch(Collection<Entity> entityList, ColumnFilter columnFilter) {
        return null;
    }

    @Override
    public List<Entity> select(Query query, ColumnFilter columnFilter) {
        var selectColumns = Arrays.stream(columnFilter.filter(tableInfo)).map(cool.scx.orm.jdbc.mapping.Column::name).toArray(String[]::new);
        var whereClauseAndWhereParams = whereParser.parseWhere(query.where());
        var groupByColumns = getGroupByColumns(query.groupBy(), tableInfo);
        var orderByClauses = getOrderByClauses(query.orderBy(), tableInfo);

        var selectStatement = table.select(selectColumns);
        if (StringUtils.notBlank(whereClauseAndWhereParams.whereClause())) {
            selectStatement.where(whereClauseAndWhereParams.whereClause());
        }
        if (groupByColumns.length > 0) {
            selectStatement.groupBy(groupByColumns);
        }
        if (orderByClauses.length > 0) {
            selectStatement.orderBy(orderByClauses);
        }
        if (query.limit().offset() != null) {
            selectStatement.offset(query.limit().offset());
        }
        if (query.limit().rowCount() != null) {
            selectStatement.limit(query.limit().rowCount());
        }

        var rowResult = selectStatement
                .bind(whereClauseAndWhereParams.whereParams())
                .execute();
        var entityBeanListHandler = new BeanListHandler<>(BeanBuilder.of(entityClass, (field) -> {
            var columnInfo = this.tableInfo.getColumn(field.getName());
            return columnInfo == null ? null : columnInfo.name();
        }));
        List<Entity> apply = null;
        try {
            apply = entityBeanListHandler.apply(rowResult);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return apply;
    }

    @Override
    public long update(Entity entity, Query query, ColumnFilter columnFilter) {
        return 0;
    }

    @Override
    public long delete(Query query) {
        return 0;
    }

    @Override
    public long count(Query query) {
        return 0;
    }

    @Override
    public Class<Entity> _entityClass() {
        return null;
    }
}

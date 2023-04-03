package cool.scx.dao.mysql_x;

import com.mysql.cj.xdevapi.Schema;
import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.Table;
import cool.scx.dao.ColumnFilter;
import cool.scx.dao.Dao;
import cool.scx.dao.Query;

import java.util.Collection;
import java.util.List;

import static cool.scx.dao.AnnotationConfigTable.initTableName;

// todo
public class MySQLXTableDao<Entity> implements Dao<Entity, String> {

    private final Session session;
    private final Schema schema;
    private final Class<Entity> entityClass;
    private final MySQLXDaoWhereParser whereParser;
    private final Table table;

    public MySQLXTableDao(Class<Entity> entityClass, Session session, String tableName) {
        this.entityClass = entityClass;
        this.session = session;
        this.schema = session.getDefaultSchema();
        this.table = schema.getTable(tableName, true);
        this.whereParser = new MySQLXDaoWhereParser();
    }

    public MySQLXTableDao(Class<Entity> entityClass, Session session) {
        this(entityClass, session, initTableName(entityClass));
    }

    @Override
    public String insert(Entity entity, ColumnFilter updateFilter) {
        return null;
    }

    @Override
    public List<String> insertBatch(Collection<Entity> entityList, ColumnFilter updateFilter) {
        return null;
    }

    @Override
    public List<Entity> select(Query query, ColumnFilter selectFilter) {
        return null;
    }

    @Override
    public long update(Entity entity, Query query, ColumnFilter updateFilter) {
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
        return entityClass;
    }

}

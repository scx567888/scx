package cool.scx.dao.mysql_x;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.JsonParser;
import com.mysql.cj.xdevapi.Schema;
import com.mysql.cj.xdevapi.Session;
import cool.scx.dao.BaseDao;
import cool.scx.dao.ColumnFilter;
import cool.scx.dao.Query;
import cool.scx.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cool.scx.dao.AnnotationConfigTable.initTableName;

public class MySQLXTableDao<Entity> implements BaseDao<Entity, String> {

    private final Session session;
    private final Schema schema;
    private final com.mysql.cj.xdevapi.Collection collection;
    private final Class<Entity> entityClass;
    private final MySQLXDaoWhereParser whereParser;

    public MySQLXTableDao(Class<Entity> entityClass, Session session, String tableName) {
        this.entityClass = entityClass;
        this.session = session;
        this.schema = session.getDefaultSchema();
        this.collection = schema.createCollection(tableName, true);
        this.whereParser = new MySQLXDaoWhereParser();
    }

    public MySQLXTableDao(Class<Entity> entityClass, Session session) {
        this(entityClass, session, initTableName(entityClass));
    }

    @Override
    public String insert(Entity entity, ColumnFilter updateFilter) {
        var dbDoc = toDbDoc(entity, updateFilter);
        var execute = this.collection.add(dbDoc).execute();
        var generatedIds = execute.getGeneratedIds();
        return generatedIds.get(0);
    }

    @Override
    public List<String> insertBatch(Collection<Entity> entityList, ColumnFilter updateFilter) {
        var dbDocs = new DbDoc[entityList.size()];
        var index = 0;
        for (var entity : entityList) {
            dbDocs[index] = toDbDoc(entity, updateFilter);
            index = index + 1;
        }
        var execute = this.collection.add(dbDocs).execute();
        return execute.getGeneratedIds();
    }

    @Override
    public List<Entity> select(Query query, ColumnFilter selectFilter) {
        var whereClauseAndWhereParams = whereParser.parseWhere(query.where());
        var findStr = whereClauseAndWhereParams.whereClause();
        var findStatement = this.collection.find(findStr).bind(whereClauseAndWhereParams.whereParams());
        if (query.limit().offset() != null) {
            findStatement.offset(query.limit().offset());
        }
        if (query.limit().rowCount() != null) {
            findStatement.limit(query.limit().rowCount());
        }
        var docResult = findStatement.execute();
        var dbDocs = docResult.fetchAll();
        var list = new ArrayList<Entity>();
        for (var dbDoc : dbDocs) {
            list.add(toEntity(dbDoc));
        }
        return list;
    }

    @Override
    public long update(Entity entity, Query query, ColumnFilter updateFilter) {
        var whereClauseAndWhereParams = whereParser.parseWhere(query.where());
        var findStr = whereClauseAndWhereParams.whereClause();
        var newDoc = toDbDoc(entity, updateFilter);
        var result = this.collection.modify(findStr).bind(whereClauseAndWhereParams.whereParams()).patch(newDoc).execute();
        return result.getAffectedItemsCount();
    }

    @Override
    public long delete(Query query) {
        var whereClauseAndWhereParams = whereParser.parseWhere(query.where());
        var findStr = whereClauseAndWhereParams.whereClause();
        var result = this.collection.remove(findStr).bind(whereClauseAndWhereParams.whereParams()).execute();
        return result.getAffectedItemsCount();
    }

    @Override
    public long count(Query query) {
        var whereClauseAndWhereParams = whereParser.parseWhere(query.where());
        var findStr = whereClauseAndWhereParams.whereClause();
        var findStatement = this.collection.find(findStr).bind(whereClauseAndWhereParams.whereParams());
        return findStatement.execute().count();
    }

    @Override
    public Class<Entity> _entityClass() {
        return entityClass;
    }

    public DbDoc toDbDoc(Object entity, ColumnFilter updateFilter) {
        String json = null;
        try {
            json = ObjectUtils.toJson(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return JsonParser.parseDoc(json);
    }

    public Entity toEntity(DbDoc dbDoc) {
        Entity entity = null;
        try {
            var json = dbDoc.toString();
            entity = ObjectUtils.jsonMapper().readValue(json, entityClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

}

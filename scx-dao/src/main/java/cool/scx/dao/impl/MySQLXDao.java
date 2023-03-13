package cool.scx.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.cj.xdevapi.*;
import cool.scx.dao.BaseDao;
import cool.scx.dao.Query;
import cool.scx.dao.SelectFilter;
import cool.scx.dao.UpdateFilter;
import cool.scx.dao.where.WhereParamsAndWhereClauses;
import cool.scx.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//todo 功能未完成
public class MySQLXDao<Entity> implements BaseDao<Entity, String> {

    private final Session session;
    private final Schema schema;
    private final com.mysql.cj.xdevapi.Collection collection;
    private final Class<Entity> entityClass;
    private final MySQLXDaoTableInfo tableInfo;

    public MySQLXDao(String tableName, Class<Entity> entityClass, Session session) {
        this.entityClass = entityClass;
        this.session = session;
        this.schema = session.getDefaultSchema();
        this.collection = schema.createCollection(tableName, true);
        this.tableInfo = new MySQLXDaoTableInfo(entityClass);
    }

    @Override
    public String insert(Entity entity, UpdateFilter updateFilter) {
        DbDoc dbDoc = toDbDoc(entity, updateFilter);
        AddResult execute = this.collection.add(dbDoc).execute();
        var generatedIds = execute.getGeneratedIds();
        return generatedIds.get(0);
    }

    @Override
    public List<String> insertBatch(Collection<Entity> entityList, UpdateFilter updateFilter) {
        DbDoc[] dbDocs = entityList.stream().map(entity -> toDbDoc(entity, updateFilter)).toArray(DbDoc[]::new);
        AddResult execute = this.collection.add(dbDocs).execute();
        return execute.getGeneratedIds();
    }

    @Override
    public List<Entity> select(Query query, SelectFilter selectFilter) {
        WhereParamsAndWhereClauses whereParamsAndWhereClauses = query.where().getWhereParamsAndWhereClauses(tableInfo);
        String findStr = String.join(" AND ", whereParamsAndWhereClauses.whereClause());
        DocResult docs = this.collection.find(findStr).bind(whereParamsAndWhereClauses.whereParams()).execute();
        List<DbDoc> dbDocs = docs.fetchAll();
        var s = new ArrayList<Entity>();
        for (DbDoc dbDoc : dbDocs) {
            s.add(toEntity(dbDoc));
        }
        return s;
    }

    @Override
    public long update(Entity entity, Query query, UpdateFilter updateFilter) {
//        DocResult docs = this.collection.modify().set().execute();
//        List<DbDoc> dbDocs = docs.fetchAll();
//        var s=new ArrayList<Entity>();
//        for (DbDoc dbDoc : dbDocs) {
//            s.add( ObjectUtils.convertValue(dbDoc,_entityClass()));
//        }
//        return s;
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

    public DbDoc toDbDoc(Object entity, UpdateFilter updateFilter) {
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

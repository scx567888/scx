package cool.scx.dao.impl.xdevapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.JsonParser;
import com.mysql.cj.xdevapi.Schema;
import com.mysql.cj.xdevapi.Session;
import cool.scx.dao.BaseDao;
import cool.scx.dao.ColumnFilter;
import cool.scx.dao.Query;
import cool.scx.dao.impl.WhereParamsAndWhereClause;
import cool.scx.dao.impl.WhereParamsAndWhereClauses;
import cool.scx.dao.query.Where;
import cool.scx.dao.query.WhereBody;
import cool.scx.sql.sql.SQL;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cool.scx.dao.AnnotationConfigTable.initTableName;
import static cool.scx.dao.impl.xdevapi.WhereTypeHandler.findWhereTypeHandler;

public class MySQLXDao<Entity> implements BaseDao<Entity, String> {

    private final Session session;
    private final Schema schema;
    private final com.mysql.cj.xdevapi.Collection collection;
    private final Class<Entity> entityClass;

    public MySQLXDao(Class<Entity> entityClass, Session session, String tableName) {
        this.entityClass = entityClass;
        this.session = session;
        this.schema = session.getDefaultSchema();
        this.collection = schema.createCollection(tableName, true);
    }

    public MySQLXDao(Class<Entity> entityClass, Session session) {
        this(entityClass, session, initTableName(entityClass));
    }

    public static WhereParamsAndWhereClauses getWhereParamsAndWhereClauses(Where where) {
        //先处理 whereBodyList
        var whereClauses = new ArrayList<String>();
        var whereParams = new ArrayList<>();
        for (WhereBody whereBody : where.whereBodyList()) {
            var whereParamsAndWhereClause = getWhereParamsAndWhereClause(whereBody);
            whereClauses.add(whereParamsAndWhereClause.whereClause());
            whereParams.addAll(List.of(whereParamsAndWhereClause.whereParams()));
        }
        //再处理 whereSQL
        var tempWhereSQL = new StringBuilder();
        for (var o : where.whereSQL()) {
            if (o instanceof String s) {
                tempWhereSQL.append(s);
            } else if (o instanceof WhereBody w) {
                var whereParamsAndWhereClause = getWhereParamsAndWhereClause(w);
                tempWhereSQL.append(whereParamsAndWhereClause.whereClause());
                whereParams.addAll(List.of(whereParamsAndWhereClause.whereParams()));
            } else if (o instanceof SQL a) {
                tempWhereSQL.append("(").append(a.sql()).append(")");
                whereParams.addAll(List.of(a.params()));
            }
        }
        var whereSQL = tempWhereSQL.toString();
        if (StringUtils.notBlank(whereSQL)) {
            whereClauses.add(whereSQL);
        }
        return new WhereParamsAndWhereClauses(whereParams.toArray(), whereClauses.toArray(String[]::new));
    }

    public static WhereParamsAndWhereClause getWhereParamsAndWhereClause(WhereBody body) {
        return findWhereTypeHandler(body.whereType()).getWhereParamsAndWhereClause(body.name(), body.whereType(), body.value1(), body.value2(), body.info());
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
        var whereParamsAndWhereClauses = getWhereParamsAndWhereClauses(query.where());
        var findStr = String.join(" AND ", whereParamsAndWhereClauses.whereClause());
        var findStatement = this.collection.find(findStr).bind(whereParamsAndWhereClauses.whereParams());
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
        var whereParamsAndWhereClauses = getWhereParamsAndWhereClauses(query.where());
        var findStr = String.join(" AND ", whereParamsAndWhereClauses.whereClause());
        var newDoc = toDbDoc(entity, updateFilter);
        var result = this.collection.modify(findStr).bind(whereParamsAndWhereClauses).patch(newDoc).execute();
        return result.getAffectedItemsCount();
    }

    @Override
    public long delete(Query query) {
        var whereParamsAndWhereClauses = getWhereParamsAndWhereClauses(query.where());
        var findStr = String.join(" AND ", whereParamsAndWhereClauses.whereClause());
        var result = this.collection.remove(findStr).bind(whereParamsAndWhereClauses.whereParams()).execute();
        return result.getAffectedItemsCount();
    }

    @Override
    public long count(Query query) {
        var whereParamsAndWhereClauses = getWhereParamsAndWhereClauses(query.where());
        var findStr = String.join(" AND ", whereParamsAndWhereClauses.whereClause());
        var findStatement = this.collection.find(findStr).bind(whereParamsAndWhereClauses.whereParams());
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

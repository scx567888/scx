package cool.scx.data.mysql_x;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.Schema;
import cool.scx.data.BaseDao;
import cool.scx.data.ColumnFilter;
import cool.scx.data.Query;
import cool.scx.util.CaseUtils;
import cool.scx.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cool.scx.data.mysql_x.JsonHelper.*;
import static cool.scx.data.mysql_x.parser.MySQLXDaoWhereParser.WHERE_PARSER;

/**
 * 使用 MySQL X Dev Api 通过 MySQL X 协议, 操作 MySQL 的 Dao
 *
 * @param <Entity>
 */
public class MySQLXDao<Entity> implements BaseDao<Entity, String> {

    private final com.mysql.cj.xdevapi.Collection collection;
    private final Class<Entity> entityClass;
    private final JsonHelper jsonHelper;

    public MySQLXDao(Class<Entity> entityClass, com.mysql.cj.xdevapi.Collection collection) {
        this.entityClass = entityClass;
        this.collection = collection;
        this.jsonHelper = new JsonHelper(entityClass);
    }

    public MySQLXDao(Class<Entity> entityClass, Schema schema) {
        this(entityClass, schema.createCollection(initCollectionName(entityClass), true));
    }

    public static String initCollectionName(Class<?> clazz) {
        var scxModel = clazz.getAnnotation(cool.scx.data.mysql_x.annotation.Collection.class);
        if (scxModel != null && StringUtils.notBlank(scxModel.name())) {
            return scxModel.name();
        }
        if (scxModel != null && StringUtils.notBlank(scxModel.prefix())) {
            return scxModel.prefix() + "_" + CaseUtils.toSnake(clazz.getSimpleName());
        }
        //这里判断一下是否使用了数据库 如果使用 则表名省略掉 数据库限定名 否则的话则添加数据库限定名
        return "scx_" + CaseUtils.toSnake(clazz.getSimpleName());
    }

    public static DbDoc toDbDoc(Object entity, ColumnFilter updateFilter) {
        var jsonNode = OBJECT_MAPPER.valueToTree(entity);
        if (jsonNode instanceof ObjectNode objectNode) {
            var newObjectNode = filterObjectNode(objectNode, updateFilter);
            return (DbDoc) toJsonValue(newObjectNode);
        }
        throw new IllegalArgumentException("jsonNode 类型不为 ObjectNode !!!");
    }

    @Override
    public String insert(Entity entity, ColumnFilter updateFilter) {
        var dbDoc = toDbDoc(entity, updateFilter.addExcluded("_id"));
        var addResult = this.collection.add(dbDoc).execute();
        var generatedIds = addResult.getGeneratedIds();
        return generatedIds.get(0);
    }

    @Override
    public List<String> insertBatch(Collection<Entity> entityList, ColumnFilter updateFilter) {
        var dbDocs = new DbDoc[entityList.size()];
        var index = 0;
        for (var entity : entityList) {
            dbDocs[index] = toDbDoc(entity, updateFilter.addExcluded("_id"));
            index = index + 1;
        }
        var addResult = this.collection.add(dbDocs).execute();
        return addResult.getGeneratedIds();
    }

    @Override
    public List<Entity> select(Query query, ColumnFilter selectFilter) {
        var whereClauseAndWhereParams = WHERE_PARSER.parseWhere(query.where());
        var findStatement = this.collection
                .find(whereClauseAndWhereParams.whereClause())
                .bind(whereClauseAndWhereParams.whereParams());
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
            list.add(toEntity(dbDoc, selectFilter));
        }
        return list;
    }

    @Override
    public long update(Entity entity, Query query, ColumnFilter updateFilter) {
        var whereClauseAndWhereParams = WHERE_PARSER.parseWhere(query.where());
        var newDoc = toDbDoc(entity, updateFilter.addExcluded("_id"));
        var result = this.collection
                .modify(whereClauseAndWhereParams.whereClause())
                .bind(whereClauseAndWhereParams.whereParams())
                .patch(newDoc)
                .execute();
        return result.getAffectedItemsCount();
    }

    @Override
    public long delete(Query query) {
        var whereClauseAndWhereParams = WHERE_PARSER.parseWhere(query.where());
        var result = this.collection
                .remove(whereClauseAndWhereParams.whereClause())
                .bind(whereClauseAndWhereParams.whereParams())
                .execute();
        return result.getAffectedItemsCount();
    }

    @Override
    public long count(Query query) {
        var whereClauseAndWhereParams = WHERE_PARSER.parseWhere(query.where());
        var docResult = this.collection
                .find(whereClauseAndWhereParams.whereClause())
                .bind(whereClauseAndWhereParams.whereParams())
                .execute();
        return docResult.count();
    }

    @Override
    public void _clear() {
        this.collection.remove("TRUE").execute();
    }

    @Override
    public Class<Entity> _entityClass() {
        return entityClass;
    }

    public Entity toEntity(DbDoc dbDoc, ColumnFilter filter) {
        try {
            var newDbDoc = filterDbDoc(dbDoc, filter);
            var objectNode = toObjectNode(newDbDoc);
            return jsonHelper.objectReader.readValue(objectNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

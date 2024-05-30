package cool.scx.data.mysql_x;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.Schema;
import cool.scx.common.field_filter.FieldFilter;
import cool.scx.common.util.CaseUtils;
import cool.scx.common.util.StringUtils;
import cool.scx.data.Dao;
import cool.scx.data.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static cool.scx.data.mysql_x.JsonHelper.*;
import static cool.scx.data.mysql_x.parser.MySQLXDaoWhereParser.WHERE_PARSER;

/**
 * 使用 MySQL X Dev Api 通过 MySQL X 协议, 操作 MySQL 的 Dao
 *
 * @param <Entity>
 */
public class MySQLXDao<Entity> implements Dao<Entity, String> {

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

    public static DbDoc toDbDoc(Object entity, FieldFilter updateFilter) {
        var jsonNode = OBJECT_MAPPER.valueToTree(entity);
        if (jsonNode instanceof ObjectNode objectNode) {
            var newObjectNode = filterObjectNode(objectNode, updateFilter);
            return (DbDoc) toJsonValue(newObjectNode);
        }
        throw new IllegalArgumentException("jsonNode 类型不为 ObjectNode !!!");
    }

    @Override
    public String add(Entity entity, FieldFilter updateFilter) {
        var dbDoc = toDbDoc(entity, updateFilter.addExcluded("_id"));
        var addResult = this.collection.add(dbDoc).execute();
        var generatedIds = addResult.getGeneratedIds();
        return generatedIds.get(0);
    }

    @Override
    public List<String> add(Collection<Entity> entityList, FieldFilter updateFilter) {
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
    public List<Entity> find(Query query, FieldFilter selectFilter) {
        var whereClause = WHERE_PARSER.parseWhere(query.getWhere());
        var findStatement = this.collection
                .find(whereClause.whereClause())
                .bind(whereClause.params());
        if (query.getOffset() != null) {
            findStatement.offset(query.getOffset());
        }
        if (query.getLimit() != null) {
            findStatement.limit(query.getLimit());
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
    public void find(Query query, FieldFilter fieldFilter, Consumer<Entity> consumer) {
        var whereClause = WHERE_PARSER.parseWhere(query.getWhere());
        var findStatement = this.collection
                .find(whereClause.whereClause())
                .bind(whereClause.params());
        if (query.getOffset() != null) {
            findStatement.offset(query.getOffset());
        }
        if (query.getLimit() != null) {
            findStatement.limit(query.getLimit());
        }
        var docResult = findStatement.execute();
        for (var dbDoc : docResult) {
            consumer.accept(toEntity(dbDoc, fieldFilter));
        }
    }

    @Override
    public Entity get(Query query, FieldFilter fieldFilter) {
        var whereClause = WHERE_PARSER.parseWhere(query.getWhere());
        var findStatement = this.collection
                .find(whereClause.whereClause())
                .bind(whereClause.params())
                .limit(1);

        var docResult = findStatement.execute();
        var dbDoc = docResult.fetchOne();

        return toEntity(dbDoc, fieldFilter);
    }

    @Override
    public long update(Entity entity, Query query, FieldFilter updateFilter) {
        var whereClause = WHERE_PARSER.parseWhere(query.getWhere());
        var newDoc = toDbDoc(entity, updateFilter.addExcluded("_id"));
        var result = this.collection
                .modify(whereClause.whereClause())
                .bind(whereClause.params())
                .patch(newDoc)
                .execute();
        return result.getAffectedItemsCount();
    }

    @Override
    public long delete(Query query) {
        var whereClause = WHERE_PARSER.parseWhere(query.getWhere());
        var result = this.collection
                .remove(whereClause.whereClause())
                .bind(whereClause.params())
                .execute();
        return result.getAffectedItemsCount();
    }

    @Override
    public long count(Query query) {
        var whereClause = WHERE_PARSER.parseWhere(query.getWhere());
        var docResult = this.collection
                .find(whereClause.whereClause())
                .bind(whereClause.params())
                .execute();
        return docResult.count();
    }

    @Override
    public void clear() {
        this.collection.remove("TRUE").execute();
    }

    @Override
    public Class<Entity> entityClass() {
        return entityClass;
    }

    public Entity toEntity(DbDoc dbDoc, FieldFilter filter) {
        try {
            var newDbDoc = filterDbDoc(dbDoc, filter);
            var objectNode = toObjectNode(newDbDoc);
            return jsonHelper.objectReader.readValue(objectNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

package cool.scx.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.DocResult;
import com.mysql.cj.xdevapi.Schema;
import com.mysql.cj.xdevapi.Session;
import cool.scx.dao.BaseDao;
import cool.scx.dao.Query;
import cool.scx.dao.SelectFilter;
import cool.scx.dao.UpdateFilter;
import cool.scx.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//todo 功能未完成
public class MySQLXDao<Entity> implements BaseDao<Entity> {

    private final Session session;
    private final Schema schema;
    private final com.mysql.cj.xdevapi.Collection collection;

    public MySQLXDao(Session session, Schema schema, com.mysql.cj.xdevapi.Collection collection) {
        this.session = session;
        this.schema = schema;
        this.collection = collection;
    }

    @Override
    public Long insert(Entity entity, UpdateFilter updateFilter) {
        String json = null;
        try {
            json = ObjectUtils.toJson(entity);
        } catch (JsonProcessingException ignored) {

        }
        this.collection.add(json).execute();
        return null;
    }

    @Override
    public List<Long> insertBatch(Collection<Entity> entityList, UpdateFilter updateFilter) {
        List<String> json = new ArrayList<>();
        for (Entity entity : entityList) {
            try {
                json.add(ObjectUtils.toJson(entity));
            } catch (JsonProcessingException ignored) {

            }
        }
        this.collection.add(json.toArray(String[]::new)).execute();
        return new ArrayList<>();
    }

    @Override
    public List<Entity> select(Query query, SelectFilter selectFilter) {
        DocResult docs = this.collection.find().execute();
        List<DbDoc> dbDocs = docs.fetchAll();

        var s = new ArrayList<Entity>();
        for (DbDoc dbDoc : dbDocs) {

            s.add(ObjectUtils.convertValue(dbDoc, _entityClass()));
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
        return null;
    }

}

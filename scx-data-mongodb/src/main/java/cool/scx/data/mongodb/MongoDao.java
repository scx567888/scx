package cool.scx.data.mongodb;

import cool.scx.data.Dao;
import cool.scx.data.Query;

import java.util.Collection;
import java.util.List;

// todo 
public class MongoDao<Entity> implements Dao<Entity, String> {

    @Override
    public String insert(Object o) {
        return null;
    }

    @Override
    public List<String> insertBatch(Collection<Entity> entityList) {
        return null;
    }

    @Override
    public List<Entity> select(Query query) {
        return null;
    }

    @Override
    public long update(Object o, Query query) {
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
    public void _clear() {

    }

    @Override
    public Class<Entity> _entityClass() {
        return null;
    }

}

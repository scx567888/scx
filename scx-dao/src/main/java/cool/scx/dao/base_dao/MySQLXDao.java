package cool.scx.dao.base_dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.cj.xdevapi.*;
import cool.scx.dao.BaseDao;
import cool.scx.dao.Query;
import cool.scx.dao.SelectFilter;
import cool.scx.dao.UpdateFilter;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.TableInfo;
import cool.scx.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySQLXDao<Entity> implements BaseDao<Entity> {

    private final Session session;
    private final Schema schema;
    private final com.mysql.cj.xdevapi.Collection collection;

    public MySQLXDao() {

        this.session = new SessionFactory().getSession("mysqlx://localhost:33060/scx_dao_test?user=root&password=root");
        //获取到某个scheme对象
        this.schema = session.createSchema("scx", true);

        //使用某个具体的collection
        this.collection = schema.createCollection("my_collection", true);
        //插入documents

//        myColl.add("{\"name\":\"Sakila\", \"age\":15}").execute();
//        myColl.add("{\"name\":\"Susanne\", \"age\":24}").execute();
//        myColl.add("{\"name\":\"User\", \"age\":39}").execute();
//
//        FindStatement findStatement = myColl.find();
//        System.out.println();
//        //查找某个document
//        DocResult docs = myColl.find("name like :name AND age < :age")
//                .bind("name", "S%").bind("age", 20).execute();
//
//        //打印文档
//        System.out.println(docs.fetchOne());
//        //关闭会话
//        mySession.close();
    }


    static class User{
        public String name;
        public Integer age;
    }

    public static void main(String[] args) {
        BaseDao<User> s=new MySQLXDao<>();
        var u=new User();
        u.name="小明";
        u.age=18;
        Long insert = s.insert(u, UpdateFilter.ofExcluded());
        List<User> select = s.select(new Query(), SelectFilter.ofExcluded());
        System.out.println();
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

        var s=new ArrayList<Entity>();
        for (DbDoc dbDoc : dbDocs) {

            s.add( ObjectUtils.convertValue(dbDoc,_entityClass()));
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
    public void _truncate() {

    }

    @Override
    public TableInfo<?> _tableInfo() {
        return null;
    }

    @Override
    public Class<Entity> _entityClass() {
        return (Class<Entity>) User.class;
    }

    @Override
    public SQLRunner _sqlRunner() {
        return null;
    }

}

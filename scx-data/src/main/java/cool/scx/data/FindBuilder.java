package cool.scx.data;

import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface FindBuilder<Entity> {

    //******************** 设置 ********************

    FindBuilder<Entity> query(Query query);

    FindBuilder<Entity> fieldPolicy(FieldPolicy fieldPolicy);


    //******************** 结果 ********************    

    List<Entity> toList();

    <T> List<T> toList(Class<T> resultType);

    List<Map<String, Object>> toMapList();

    void forEach(Consumer<Entity> entityConsumer);

    <T> void forEach(Consumer<T> entityConsumer, Class<T> resultType);

    void forEachMap(Consumer<Map<String, Object>> entityConsumer);

    Entity getFirst();

    <T> T getFirst(Class<T> resultType);

    Map<String, Object> getFirstMap();

    long count();

}

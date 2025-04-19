package cool.scx.data;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/// 查询执行器
///
/// @param <Entity>
/// @author scx567888
/// @version 0.0.1
public interface FindExecutor<Entity> {

    List<Entity> list();

    <T> List<T> list(Class<T> resultType);

    List<Map<String, Object>> mapList();

    void forEach(Consumer<Entity> entityConsumer);

    <T> void forEach(Consumer<T> entityConsumer, Class<T> resultType);

    void forEachMap(Consumer<Map<String, Object>> entityConsumer);

    Entity first();

    <T> T first(Class<T> resultType);

    Map<String, Object> firstMap();

    long count();

}

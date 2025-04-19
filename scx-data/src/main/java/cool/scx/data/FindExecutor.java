package cool.scx.data;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 查询执行器
 *
 * @param <Entity>
 */
public interface FindExecutor<Entity> {

    List<Entity> fetchList();

    <T> List<T> fetchList(Class<T> resultType);

    List<Map<String, Object>> fetchMapList();

    void forEach(Consumer<Entity> entityConsumer);

    <T> void forEach(Consumer<T> entityConsumer, Class<T> resultType);

    void forEachMap(Consumer<Map<String, Object>> entityConsumer);

    Entity fetchFirst();

    <T> T fetchFirst(Class<T> resultType);

    Map<String, Object> fetchFirstMap();

    long count();

}

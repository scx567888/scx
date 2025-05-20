package cool.scx.data;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/// 聚合器
///
/// @author scx567888
/// @version 0.0.1
public interface Aggregator {

    <T> List<T> list(Class<T> resultType);

    List<Map<String, Object>> list();

    <T> void forEach(Consumer<T> resultConsumer, Class<T> resultType);

    void forEach(Consumer<Map<String, Object>> resultConsumer);

    <T> T first(Class<T> resultType);

    Map<String, Object> first();

}

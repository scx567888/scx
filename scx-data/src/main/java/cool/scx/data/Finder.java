package cool.scx.data;

import cool.scx.data.exception.DataAccessException;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/// 查询执行器
///
/// @param <Entity>
/// @author scx567888
/// @version 0.0.1
public interface Finder<Entity> {

    List<Entity> list()  throws DataAccessException;

    <T> List<T> list(Class<T> resultType)  throws DataAccessException;

    List<Map<String, Object>> listMap()  throws DataAccessException;

    void forEach(Consumer<Entity> entityConsumer)  throws DataAccessException;

    <T> void forEach(Consumer<T> entityConsumer, Class<T> resultType)  throws DataAccessException;

    void forEachMap(Consumer<Map<String, Object>> entityConsumer)  throws DataAccessException;

    Entity first()  throws DataAccessException;

    <T> T first(Class<T> resultType)  throws DataAccessException;

    Map<String, Object> firstMap()  throws DataAccessException;

    long count()  throws DataAccessException;

}

package cool.scx.data;

import cool.scx.data.exception.DataAccessException;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/// 聚合器
///
/// @author scx567888
/// @version 0.0.1
public interface Aggregator {

    <T> List<T> list(Class<T> resultType)  throws DataAccessException;

    List<Map<String, Object>> list()  throws DataAccessException;

    <T> void forEach(Consumer<T> resultConsumer, Class<T> resultType)  throws DataAccessException;

    void forEach(Consumer<Map<String, Object>> resultConsumer)  throws DataAccessException;

    <T> T first(Class<T> resultType)  throws DataAccessException;

    Map<String, Object> first()  throws DataAccessException;

}

package cool.scx.sql.result_handler;

import cool.scx.functional.ScxFunction;
import cool.scx.sql.bean_builder.BeanBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * <p>ResultHandler interface.</p>
 *
 * @author scx567888
 * @version 0.0.7
 */
public interface ResultHandler<T> extends ScxFunction<ResultSet, T, SQLException> {

    static ResultHandler<Map<String, Object>> ofMap() {
        return MapHandler.INSTANCE;
    }

    static ResultHandler<Map<String, Object>> ofMap(Supplier<Map<String, Object>> mapSupplier) {
        return new MapHandler(mapSupplier);
    }

    static ResultHandler<List<Map<String, Object>>> ofMapList() {
        return MapListHandler.INSTANCE;
    }

    static ResultHandler<List<Map<String, Object>>> ofMapList(Supplier<Map<String, Object>> mapSupplier) {
        return new MapListHandler(mapSupplier);
    }

    static <C> ResultHandler<C> ofBean(Class<C> clazz) {
        return new BeanHandler<>(BeanBuilder.of(clazz));
    }

    static <C> ResultHandler<List<C>> ofBeanList(Class<C> clazz) {
        return new BeanListHandler<>(BeanBuilder.of(clazz));
    }

    static <C> ResultHandler<C> ofSingleValue(String columnName, Class<C> clazz) {
        return new SingleValueHandler<>(columnName, clazz);
    }

    static <C> ResultHandler<C> ofSingleValue(int columnIndex, Class<C> clazz) {
        return new SingleValueHandler<>(columnIndex, clazz);
    }

}

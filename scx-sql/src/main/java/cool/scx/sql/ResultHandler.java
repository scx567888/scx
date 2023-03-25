package cool.scx.sql;

import cool.scx.functional.ScxFunction;
import cool.scx.sql.result_handler.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>ResultHandler interface.</p>
 *
 * @author scx567888
 * @version 0.0.7
 */
public interface ResultHandler<T> extends ScxFunction<ResultSet, T, SQLException> {

    static MapHandler ofMap() {
        return new MapHandler();
    }

    static MapListHandler ofMapList() {
        return new MapListHandler();
    }

    static <C> BeanHandler<C> ofBean(Class<C> clazz) {
        return new BeanHandler<>(BeanBuilder.of(clazz));
    }

    static <C> BeanListHandler<C> ofBeanList(Class<C> clazz) {
        return new BeanListHandler<>(BeanBuilder.of(clazz));
    }

    static <C> SingleValueHandler<C> ofSingleValue(String columnName, Class<C> clazz) {
        return new SingleValueHandler<>(columnName, clazz);
    }

}

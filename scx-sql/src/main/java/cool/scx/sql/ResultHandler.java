package cool.scx.sql;

import cool.scx.functional.ScxHandlerARE;
import cool.scx.functional.ScxHandlerR;
import cool.scx.sql.result_handler.BeanListHandler;
import cool.scx.sql.result_handler.MapListHandler;
import cool.scx.sql.result_handler.SingleValueHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <p>ResultHandler interface.</p>
 *
 * @author scx567888
 * @version 0.0.7
 */
public interface ResultHandler<T> extends ScxHandlerARE<ResultSet, T, SQLException> {

    /**
     * <p>ofBeanList.</p>
     *
     * @param type a {@link java.lang.Class} object
     * @param <T>  a T class
     * @return a {@link cool.scx.sql.ResultHandler} object
     */
    static <T> ResultHandler<List<T>> ofBeanList(Class<T> type) {
        return new BeanListHandler<>(type);
    }

    /**
     * <p>ofMapList.</p>
     *
     * @return a {@link cool.scx.sql.ResultHandler} object
     */
    static ResultHandler<List<Map<String, Object>>> ofMapList() {
        return MapListHandler.DEFAULT_MAP_LIST_HANDLER;
    }

    /**
     * a
     *
     * @param mapSupplier a
     * @return a
     */
    static ResultHandler<List<Map<String, Object>>> ofMapList(ScxHandlerR<Map<String, Object>> mapSupplier) {
        return new MapListHandler(mapSupplier);
    }

    /**
     * <p>ofObject.</p>
     *
     * @param columnName a {@link java.lang.String} object
     * @param clazz      a {@link java.lang.Class} object
     * @param <T>        a T class
     * @return a {@link cool.scx.sql.ResultHandler} object
     */
    static <T> ResultHandler<T> ofSingleValue(String columnName, Class<T> clazz) {
        return new SingleValueHandler<>(columnName, clazz);
    }

    /**
     * <p>ofObject.</p>
     *
     * @param columnIndex a int
     * @param clazz       a {@link java.lang.Class} object
     * @param <T>         a T class
     * @return a {@link cool.scx.sql.ResultHandler} object
     */
    static <T> ResultHandler<T> ofSingleValue(int columnIndex, Class<T> clazz) {
        return new SingleValueHandler<>(columnIndex, clazz);
    }

}

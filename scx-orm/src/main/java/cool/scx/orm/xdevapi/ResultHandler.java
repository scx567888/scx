package cool.scx.orm.xdevapi;

import com.mysql.cj.xdevapi.RowResult;
import cool.scx.functional.ScxFunction;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

/**
 * <p>ResultHandler interface.</p>
 *
 * @author scx567888
 * @version 0.0.7
 */
public interface ResultHandler<T> extends ScxFunction<RowResult, T, SQLException> {


    static <C> ResultHandler<List<C>> ofBeanList(Class<C> clazz) {
        return new BeanListHandler<>(BeanBuilder.of(clazz));
    }

    static <C> ResultHandler<List<C>> ofBeanList(Class<C> clazz, Function<Field, String> columnNameMapping) {
        return new BeanListHandler<>(BeanBuilder.of(clazz, columnNameMapping));
    }


    /**
     * 执行前
     *
     * @param preparedStatement a
     * @return a
     * @throws SQLException a
     */
    default PreparedStatement beforeExecuteQuery(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement;
    }

}

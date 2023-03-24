package cool.scx.sql;

import cool.scx.sql.bean_builder.NormalBeanBuilder;
import cool.scx.sql.bean_builder.RecordBeanBuilder;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public interface BeanBuilder<T> {

    static <T> BeanBuilder<T> of(Class<T> type) {
        return type.isRecord() ? new RecordBeanBuilder<>(type) : new NormalBeanBuilder<>(type);
    }

    static <T> BeanBuilder<T> of(Class<T> type, Function<Field, String> columnNameMapping) {
        return type.isRecord() ? new RecordBeanBuilder<>(type, columnNameMapping) : new NormalBeanBuilder<>(type, columnNameMapping);
    }

    T createBean(ResultSet rs, int[] indexInfo) throws SQLException;

    FieldSetter[] fieldSetters();

}

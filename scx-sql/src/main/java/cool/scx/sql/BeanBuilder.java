package cool.scx.sql;

import cool.scx.sql.bean_builder.NormalBeanBuilder;
import cool.scx.sql.bean_builder.RecordBeanBuilder;
import cool.scx.sql.mapping.TableInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    static <T> BeanBuilder<T> of(Class<T> type, TableInfo<?> tableInfo) {
        return type.isRecord() ? new RecordBeanBuilder<>(type, tableInfo) : new NormalBeanBuilder<>(type, tableInfo);
    }

    T createBean(ResultSet rs, int[] indexInfo) throws SQLException;

    FieldSetter[] fieldSetters();

}

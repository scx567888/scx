package cool.scx.sql;

import cool.scx.sql.bean_builder.NormalBeanBuilder;
import cool.scx.sql.bean_builder.RecordBeanBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public interface BeanBuilder<T> {

    /**
     * <p>of.</p>
     *
     * @param type a {@link java.lang.Class} object
     * @param <T>  a T class
     * @return a {@link cool.scx.sql.BeanBuilder} object
     */
    static <T> BeanBuilder<T> of(Class<T> type) {
        if (type.isRecord()) {
            return new RecordBeanBuilder<>(type);
        } else {
            return new NormalBeanBuilder<>(type);
        }
    }

    /**
     * <p>createBean.</p>
     *
     * @param rs        a ResultSet object
     * @param indexInfo an array of {@link int} objects
     * @return a T object
     * @throws java.sql.SQLException if any.
     */
    T createBean(ResultSet rs, int[] indexInfo) throws SQLException;

    /**
     * <p>fieldSetters.</p>
     *
     * @return an array of {@link cool.scx.sql.FieldSetter} objects
     */
    FieldSetter[] fieldSetters();

}

package cool.scx.sql.result_handler;

import cool.scx.sql.bean_builder.BeanBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
record BeanHandler<T>(BeanBuilder<T> beanBuilder) implements ResultHandler<T> {

    @Override
    public T apply(ResultSet rs) throws SQLException {
        var indexInfo = beanBuilder.getIndexInfo(rs.getMetaData());
        return rs.next() ? beanBuilder.createBean(rs, indexInfo) : null;
    }

}

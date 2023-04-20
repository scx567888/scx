package cool.scx.data.jdbc.result_handler;

import cool.scx.data.jdbc.bean_builder.BeanBuilder;
import cool.scx.data.jdbc.dialect.Dialect;

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
    public T apply(ResultSet rs, Dialect typeHandlerSelector) throws SQLException {
        beanBuilder.setTypeHandlerSelector(typeHandlerSelector);
        var indexInfo = beanBuilder.getIndexInfo(rs.getMetaData());
        return rs.next() ? beanBuilder.createBean(rs, indexInfo) : null;
    }

}

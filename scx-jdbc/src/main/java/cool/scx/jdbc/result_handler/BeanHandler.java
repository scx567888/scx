package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.bean_builder.BeanBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/// BeanHandler
///
/// @author scx567888
/// @version 0.0.1
record BeanHandler<T>(BeanBuilder<T> beanBuilder) implements ResultHandler<T, RuntimeException> {

    @Override
    public T apply(ResultSet rs, Dialect dialect) throws SQLException {
        beanBuilder.bindDialect(dialect);
        var indexInfo = beanBuilder.getIndexInfo(rs.getMetaData());
        return rs.next() ? beanBuilder.createBean(rs, indexInfo) : null;
    }

}

package cool.scx.jdbc.result_handler;

import cool.scx.function.Function1Void;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.bean_builder.BeanBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/// BeanConsumerHandler
///
/// @author scx567888
/// @version 0.0.1
record BeanConsumerHandler<T, X extends Throwable>(BeanBuilder<T> beanBuilder,
                                                   Function1Void<T, X> consumer) implements ResultHandler<Void, X> {

    @Override
    public Void apply(ResultSet rs, Dialect dialect) throws SQLException, X {
        beanBuilder.bindDialect(dialect);
        var indexInfo = beanBuilder.getIndexInfo(rs.getMetaData());
        while (rs.next()) {
            T t = beanBuilder.createBean(rs, indexInfo);
            consumer.apply(t);
        }
        return null;
    }

}

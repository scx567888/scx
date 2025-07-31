package cool.scx.jdbc.result_handler;

import cool.scx.function.ConsumerX;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.bean_builder.BeanBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/// BeanConsumerHandler
///
/// @author scx567888
/// @version 0.0.1
record BeanConsumerHandler<T, E extends Throwable>(BeanBuilder<T> beanBuilder,
                                                   ConsumerX<T, E> consumer) implements ResultHandler<Void, E> {

    @Override
    public Void apply(ResultSet rs, Dialect dialect) throws SQLException, E {
        beanBuilder.bindDialect(dialect);
        var indexInfo = beanBuilder.getIndexInfo(rs.getMetaData());
        while (rs.next()) {
            T t = beanBuilder.createBean(rs, indexInfo);
            consumer.accept(t);
        }
        return null;
    }

}

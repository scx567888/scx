package cool.scx.data.jdbc.result_handler;

import cool.scx.data.jdbc.bean_builder.BeanBuilder;
import cool.scx.data.jdbc.dialect.Dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
record BeanConsumerHandler<T>(BeanBuilder<T> beanBuilder, Consumer<T> consumer) implements ResultHandler<Void> {

    @Override
    public Void apply(ResultSet rs, Dialect typeHandlerSelector) throws SQLException {
        beanBuilder.setTypeHandlerSelector(typeHandlerSelector);
        var indexInfo = beanBuilder.getIndexInfo(rs.getMetaData());
        while (rs.next()) {
            T t = beanBuilder.createBean(rs, indexInfo);
            consumer.accept(t);
        }
        return null;
    }

}

package cool.scx.sql.result_handler;

import cool.scx.sql.bean_builder.BeanBuilder;

import java.sql.PreparedStatement;
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
    public Void apply(ResultSet rs) throws SQLException {
        var indexInfo = beanBuilder.getIndexInfo(rs.getMetaData());
        while (rs.next()) {
            T t = beanBuilder.createBean(rs, indexInfo);
            consumer.accept(t);
        }
        return null;
    }

    @Override
    public PreparedStatement beforeExecuteQuery(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setFetchSize(Integer.MIN_VALUE);
        return preparedStatement;
    }

}

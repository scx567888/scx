package cool.scx.sql.result_handler;

import cool.scx.sql.ResultStream;
import cool.scx.sql.bean_builder.BeanBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * a
 *
 * @param beanBuilder a
 * @param <T>         a
 */
record BeanStreamHandler<T>(BeanBuilder<T> beanBuilder) implements ResultHandler<ResultStream<T>> {

    @Override
    public ResultStream<T> apply(ResultSet rs) throws SQLException {
        var indexInfo = beanBuilder.getIndexInfo(rs.getMetaData());
        return new ResultStream<>(c -> beanBuilder.createBean(c, indexInfo), rs);
    }

}

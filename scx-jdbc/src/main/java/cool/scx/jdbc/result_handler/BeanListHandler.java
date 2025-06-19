package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.bean_builder.BeanBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/// BeanListHandler
///
/// @author scx567888
/// @version 0.0.1
record BeanListHandler<T>(BeanBuilder<T> beanBuilder) implements ResultHandler<List<T>, RuntimeException> {

    @Override
    public List<T> apply(ResultSet rs, Dialect dialect) throws SQLException {
        beanBuilder.bindDialect(dialect);
        var indexInfo = beanBuilder.getIndexInfo(rs.getMetaData());
        var list = new ArrayList<T>();
        //从rs中取出数据, 并且封装到ArrayList中
        while (rs.next()) {
            T t = beanBuilder.createBean(rs, indexInfo);
            list.add(t);
        }
        return list;
    }

}

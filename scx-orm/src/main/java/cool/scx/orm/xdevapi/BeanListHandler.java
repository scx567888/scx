package cool.scx.orm.xdevapi;

import com.mysql.cj.xdevapi.Row;
import com.mysql.cj.xdevapi.RowResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
record BeanListHandler<T>(BeanBuilder<T> beanBuilder) implements ResultHandler<List<T>> {

    /**
     * {@inheritDoc}
     * a
     */
    @Override
    public List<T> apply(RowResult rs) throws SQLException {
        var indexInfo = beanBuilder.getIndexInfo(rs);
        var list = new ArrayList<T>();
        //从rs中取出数据，并且封装到ArrayList中
        for (Row r : rs) {
            T t = beanBuilder.createBean(r, indexInfo);
            list.add(t);
        }
        return list;
    }

}

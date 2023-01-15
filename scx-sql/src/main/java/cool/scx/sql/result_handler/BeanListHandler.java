package cool.scx.sql.result_handler;


import cool.scx.sql.BeanBuilder;
import cool.scx.sql.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.sql.FieldSetter.getIndexInfo;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public class BeanListHandler<T> implements ResultHandler<List<T>> {

    protected final BeanBuilder<T> beanBuilder;

    /**
     * a
     *
     * @param type a
     */
    public BeanListHandler(Class<T> type) {
        this.beanBuilder = BeanBuilder.of(type);
    }

    /**
     * {@inheritDoc}
     * a
     */
    @Override
    public List<T> apply(ResultSet rs) throws SQLException {
        var indexInfo = getIndexInfo(rs.getMetaData(), beanBuilder.fieldSetters());
        var list = new ArrayList<T>();
        //从rs中取出数据，并且封装到ArrayList中
        while (rs.next()) {
            T t = beanBuilder.createBean(rs, indexInfo);
            list.add(t);
        }
        return list;
    }

}

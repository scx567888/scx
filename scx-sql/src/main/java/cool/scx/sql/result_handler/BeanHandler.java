package cool.scx.sql.result_handler;

import cool.scx.sql.BeanBuilder;
import cool.scx.sql.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

import static cool.scx.sql.FieldSetter.getIndexInfo;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public class BeanHandler<T> implements ResultHandler<T> {

    protected final BeanBuilder<T> beanBuilder;

    public BeanHandler(BeanBuilder<T> beanBuilder) {
        this.beanBuilder = beanBuilder;
    }

    @Override
    public T apply(ResultSet rs) throws SQLException {
        var indexInfo = getIndexInfo(rs.getMetaData(), beanBuilder.fieldSetters());
        return rs.next() ? beanBuilder.createBean(rs, indexInfo) : null;
    }

}

package cool.scx.sql.result_handler;

import cool.scx.sql.BeanBuilder;
import cool.scx.sql.FieldSetter;
import cool.scx.sql.ResultHandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

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

    /**
     * 返回 fieldSetters 索引对应的 rsm 的索引数组 若无对应则使用 -1 占位
     *
     * @param rsm          rsm
     * @param fieldSetters f
     * @return f
     * @throws SQLException f
     */
    static int[] getIndexInfo(ResultSetMetaData rsm, FieldSetter[] fieldSetters) throws SQLException {
        var count = rsm.getColumnCount();
        var nameIndexMap = new HashMap<String, Integer>();
        for (int i = 1; i <= count; i = i + 1) {
            nameIndexMap.put(rsm.getColumnLabel(i), i);
        }
        var indexInfo = new int[fieldSetters.length];
        for (int i = 0; i < fieldSetters.length; i = i + 1) {
            indexInfo[i] = nameIndexMap.getOrDefault(fieldSetters[i].columnName(), -1);
        }
        return indexInfo;
    }

    @Override
    public T apply(ResultSet rs) throws SQLException {
        var indexInfo = getIndexInfo(rs.getMetaData(), beanBuilder.fieldSetters());
        return rs.next() ? beanBuilder.createBean(rs, indexInfo) : null;
    }

}

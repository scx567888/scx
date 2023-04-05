package cool.scx.sql.bean_builder;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.function.Function;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public interface BeanBuilder<T> {

    static <T> BeanBuilder<T> of(Class<T> type) {
        return type.isRecord() ? new RecordBeanBuilder<>(type) : new NormalBeanBuilder<>(type);
    }

    static <T> BeanBuilder<T> of(Class<T> type, Function<Field, String> columnNameMapping) {
        return type.isRecord() ? new RecordBeanBuilder<>(type, columnNameMapping) : new NormalBeanBuilder<>(type, columnNameMapping);
    }

    T createBean(ResultSet rs, int[] indexInfo) throws SQLException;

    FieldSetter[] fieldSetters();

    /**
     * 返回 fieldSetters 索引对应的 rsm 的索引数组 若无对应则使用 -1 占位
     *
     * @param rsm rsm
     * @return f
     * @throws SQLException f
     */
    default int[] getIndexInfo(ResultSetMetaData rsm) throws SQLException {
        var count = rsm.getColumnCount();
        var nameIndexMap = new HashMap<String, Integer>();
        for (int i = 1; i <= count; i = i + 1) {
            nameIndexMap.put(rsm.getColumnLabel(i), i);
        }
        var setters = fieldSetters();
        var indexInfo = new int[setters.length];
        for (int i = 0; i < setters.length; i = i + 1) {
            indexInfo[i] = nameIndexMap.getOrDefault(setters[i].columnName(), -1);
        }
        return indexInfo;
    }

}

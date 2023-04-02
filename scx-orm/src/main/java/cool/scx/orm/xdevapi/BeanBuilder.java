package cool.scx.orm.xdevapi;

import com.mysql.cj.xdevapi.Row;
import com.mysql.cj.xdevapi.RowResult;

import java.lang.reflect.Field;
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

    T createBean(Row rs, int[] indexInfo) throws SQLException;

    FieldSetter[] fieldSetters();

    /**
     * 返回 fieldSetters 索引对应的 rsm 的索引数组 若无对应则使用 -1 占位
     *
     * @param rsm rsm
     * @return f
     * @throws SQLException f
     */
    default int[] getIndexInfo(RowResult rsm) throws SQLException {
        var nameIndexMap = new HashMap<String, Integer>();
        var columns = rsm.getColumnNames();
        var index = 0;
        for (var column : columns) {
            nameIndexMap.put(column, index);
            index = index + 1;
        }
        var setters = fieldSetters();
        var indexInfo = new int[setters.length];
        for (int i = 0; i < setters.length; i = i + 1) {
            indexInfo[i] = nameIndexMap.getOrDefault(setters[i].columnName(), -1);
        }
        return indexInfo;
    }

}

package cool.scx.sql;

import cool.scx.util.reflect.FieldUtils;

import java.lang.reflect.Field;
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
public final class FieldSetter {

    private final Field javaField;
    private final String columnName;
    private final TypeHandler<?> typeHandler;

    public FieldSetter(Field field, String columnName) {
        this.javaField = field;
        this.columnName = columnName;
        var fieldGenericType = field.getGenericType();
        this.javaField.setAccessible(true);
        this.typeHandler = TypeHandlerRegistry.getTypeHandler(fieldGenericType);
    }

    /**
     * 返回 fieldSetters 索引对应的 rsm 的索引数组 若无对应则使用 -1 占位
     *
     * @param rsm          rsm
     * @param fieldSetters f
     * @return f
     * @throws SQLException f
     */
    public static int[] getIndexInfo(ResultSetMetaData rsm, FieldSetter[] fieldSetters) throws SQLException {
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

    public static FieldSetter[] ofArray(Class<?> type, TableInfo<?> tableInfo) {
        var fields = FieldUtils.findFields(type);
        var fieldSetters = new FieldSetter[fields.length];
        if (tableInfo == null) {
            for (int i = 0; i < fields.length; i = i + 1) {
                fieldSetters[i] = of(fields[i]);
            }
        } else {
            for (int i = 0; i < fields.length; i = i + 1) {
                fieldSetters[i] = of(fields[i], tableInfo.getColumnInfo(fields[i].getName()));
            }
        }
        return fieldSetters;
    }

    public static FieldSetter of(Field field, ColumnInfo columnInfo) {
        return new FieldSetter(field, columnInfo == null ? field.getName() : columnInfo.columnName());
    }

    public static FieldSetter of(Field field) {
        return of(field, null);
    }

    public final void set(Object t, ResultSet s, int index) throws SQLException {
        var o = getObject(s, index);
        if (o != null) {
            try {
                this.javaField.set(t, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String columnName() {
        return columnName;
    }

    public Field javaField() {
        return javaField;
    }

    public Object getObject(ResultSet s, int index) throws SQLException {
        return typeHandler.getObject(s, index);
    }

}
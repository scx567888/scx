package cool.scx.sql;

import cool.scx.sql.field_setter.EnumFieldSetter;
import cool.scx.sql.field_setter.JsonFieldSetter;
import cool.scx.sql.field_setter.NormalFieldSetter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
public abstract class FieldSetter {

    protected final Field field;
    protected final String columnName;
    protected final Type fieldGenericType;

    public FieldSetter(Field field, ColumnInfo columnInfo) {
        this.field = field;
        this.columnName = columnInfo == null ? field.getName() : columnInfo.columnName();
        this.fieldGenericType = field.getGenericType();
        this.field.setAccessible(true);
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

    public static FieldSetter of(Field field, ColumnInfo columnInfo) {
        var filedType = field.getType();
        if (SQLHelper.getMySQLType(filedType) != null) {
            return new NormalFieldSetter(field, columnInfo);
        } else if (filedType.isEnum()) {
            return new EnumFieldSetter(field, columnInfo);
        } else {
            return new JsonFieldSetter(field, columnInfo);
        }
    }

    public static FieldSetter of(Field field) {
        return of(field, null);
    }

    public final void set(Object t, ResultSet s, int index) throws SQLException {
        var o = getObject(s, index);
        if (o != null) {
            try {
                this.field.set(t, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public final String columnName() {
        return columnName;
    }

    public abstract Object getObject(ResultSet s, int index) throws SQLException;

}
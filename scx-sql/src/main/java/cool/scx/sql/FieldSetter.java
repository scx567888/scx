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
import java.util.Map;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public abstract class FieldSetter {

    protected final Field field;

    protected final Type fieldGenericType;

    public FieldSetter(Field field) {
        this.field = field;
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
            indexInfo[i] = nameIndexMap.getOrDefault(fieldSetters[i].getName(), -1);
        }
        return indexInfo;
    }

    public static Map<String, FieldSetter> ofMap(Field[] fields) {
        var map = new HashMap<String, FieldSetter>();
        for (var field : fields) {
            map.put(field.getName(), of(field));
        }
        return map;
    }

    public static FieldSetter[] ofArray(Field[] fields) {
        var arr = new FieldSetter[fields.length];
        for (int i = 0; i < fields.length; i = i + 1) {
            arr[i] = of(fields[i]);
        }
        return arr;
    }

    public static FieldSetter of(Field field) {
        var filedType = field.getType();
        if (SQLHelper.getMySQLType(filedType) != null) {
            return new NormalFieldSetter(field);
        } else if (filedType.isEnum()) {
            return new EnumFieldSetter(field);
        } else {
            return new JsonFieldSetter(field);
        }
    }

    public void set(Object t, ResultSet s, int index) throws SQLException {
        var o = getObject(s, index);
        if (o != null) {
            try {
                this.field.set(t, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return field.getName();
    }

    public abstract Object getObject(ResultSet s, int index) throws SQLException;

}
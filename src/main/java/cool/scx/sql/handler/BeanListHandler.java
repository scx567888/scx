package cool.scx.sql.handler;

import cool.scx.ScxHandlerRE;
import cool.scx.sql.SQLTypeHelper;
import cool.scx.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * a
 *
 * @param <T> a
 * @author scx567888
 * @version 1.7.3
 */
public record BeanListHandler<T>(Class<? extends T> type) implements ScxHandlerRE<ResultSet, List<T>, SQLException> {

    /**
     * 读取 json 值 或者返回 null
     *
     * @param json        s
     * @param genericType g
     * @return r
     */
    private static Object readJsonValueOrNull(String json, Type genericType) {
        if (json != null) {
            try {
                return ObjectUtils.jsonMapper().readValue(json, ObjectUtils.constructType(genericType));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * a
     */
    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        var list = new ArrayList<T>();
        try {
            var rsm = rs.getMetaData();
            var count = rsm.getColumnCount();
            var allField = new Field[count + 1];
            for (int i = 1; i <= count; i++) {
                try {
                    allField[i] = type.getField(rsm.getColumnLabel(i));
                    allField[i].setAccessible(true);
                } catch (Exception e) {
                    allField[i] = null;
                }
            }
            //从rs中取出数据，并且封装到ArrayList中
            while (rs.next()) {
                T t = type.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= count; i++) {
                    var field = allField[i];
                    if (field != null) {
                        var filedType = field.getType();
                        Object o;
                        if (SQLTypeHelper.getMySQLType(filedType) != null) {
                            o = rs.getObject(i, filedType);
                        } else if (filedType.isEnum()) {
                            o = ObjectUtils.convertValue(rs.getString(i), filedType);
                        } else {
                            o = readJsonValueOrNull(rs.getString(i), field.getGenericType());
                        }
                        //这里如果数据库中为空 则不进行赋值
                        if (o != null) {
                            field.set(t, o);
                        }
                    }
                }
                list.add(t);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return list;
    }

}

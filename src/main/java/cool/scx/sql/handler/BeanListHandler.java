package cool.scx.sql.handler;

import cool.scx.functional.ScxHandlerRE;
import cool.scx.sql.SQLTypeHelper;
import cool.scx.util.ScanClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a
 *
 * @author scx567888
 * @version 1.7.3
 */
public final class BeanListHandler<T> implements ScxHandlerRE<ResultSet, List<T>, SQLException> {

    private final Constructor<T> tConstructor;

    /**
     * 使用 map 存储提高根据 名称查找 field 的速度
     */
    private final Map<String, Field> nameFieldMap = new HashMap<>();

    public BeanListHandler(Class<T> type) {
        if (ScanClassUtils.isNormalClass(type)) {
            try {
                this.tConstructor = type.getConstructor();
                this.tConstructor.newInstance(); // 尝试进行一次实例化
                for (var field : type.getFields()) {
                    field.setAccessible(true);
                    nameFieldMap.put(field.getName(), field);
                }
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException("Class : " + type + " ;无法被实例化", e);
            }
        } else {
            throw new IllegalArgumentException("Class : " + type + " ;无法被实例化");
        }
    }

    /**
     * {@inheritDoc}
     * a
     */
    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        var rsm = rs.getMetaData();
        var count = rsm.getColumnCount();
        var allField = new Field[count + 1];
        for (int i = 1; i <= count; i++) {
            allField[i] = nameFieldMap.getOrDefault(rsm.getColumnLabel(i), null);
        }
        var list = new ArrayList<T>();
        //从rs中取出数据，并且封装到ArrayList中
        while (rs.next()) {
            T t = newInstance();
            for (int i = 1; i <= count; i++) {
                var field = allField[i];
                if (field != null) {
                    var filedType = field.getType();
                    Object o;
                    if (SQLTypeHelper.getMySQLType(filedType) != null) {
                        o = rs.getObject(i, filedType);
                    } else if (filedType.isEnum()) {
                        o = SQLTypeHelper.readFromValueOrNull(rs.getString(i), filedType);
                    } else {
                        o = SQLTypeHelper.readFromJsonValueOrNull(rs.getString(i), field.getGenericType());
                    }
                    //这里如果数据库中为空 则不进行赋值
                    if (o != null) {
                        try {
                            field.set(t, o);//这里理论上是不会抛出异常的
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            list.add(t);
        }
        return list;
    }

    private T newInstance() {
        try {
            return tConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //理论上讲是不会走到这里 ,因为会在构造函数中进行校验
            e.printStackTrace();
            return null;
        }
    }

}

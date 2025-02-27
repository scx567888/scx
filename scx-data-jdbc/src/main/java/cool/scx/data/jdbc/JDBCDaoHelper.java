package cool.scx.data.jdbc;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.data.field_filter.FieldFilter;
import cool.scx.jdbc.JDBCType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import static cool.scx.jdbc.JDBCType.JSON;
import static cool.scx.jdbc.JDBCType.VARCHAR;
import static cool.scx.common.util.ClassUtils.isEnum;

/**
 * 列过滤器
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class JDBCDaoHelper {

    /**
     * 过滤
     *
     * @param tableInfo 带过滤的列表
     * @return 过滤后的列表
     */
    public static AnnotationConfigColumn[] filter(FieldFilter fieldFilter, AnnotationConfigTable tableInfo) {
        return fieldFilter.getFieldNames().length == 0 ?
                switch (fieldFilter.getFilterMode()) {
                    case INCLUDED -> new AnnotationConfigColumn[0];
                    case EXCLUDED -> tableInfo.columns();
                } :
                switch (fieldFilter.getFilterMode()) {
                    case INCLUDED -> {
                        var list = new ArrayList<AnnotationConfigColumn>();
                        for (var fieldName : fieldFilter.getFieldNames()) {
                            list.add(tableInfo.getColumn(fieldName));
                        }
                        yield list.toArray(AnnotationConfigColumn[]::new);
                    }
                    case EXCLUDED -> {
                        var objects = new ArrayList<>(Arrays.asList(tableInfo.columns()));
                        for (var fieldName : fieldFilter.getFieldNames()) {
                            objects.remove(tableInfo.getColumn(fieldName));
                        }
                        yield objects.toArray(AnnotationConfigColumn[]::new);
                    }
                };
    }

    /**
     * 过滤
     *
     * @param entity    a
     * @param tableInfo 带过滤的列表
     * @return 过滤后的列表
     */
    public static AnnotationConfigColumn[] filter(FieldFilter fieldFilter, Object entity, AnnotationConfigTable tableInfo) {
        return fieldFilter.getIgnoreNullValue() ? excludeIfFieldValueIsNull(entity, filter(fieldFilter, tableInfo)) : filter(fieldFilter, tableInfo);
    }

    /**
     * 过滤空值
     *
     * @param entity            e
     * @param scxDaoColumnInfos s
     * @return e
     */
    private static AnnotationConfigColumn[] excludeIfFieldValueIsNull(Object entity, AnnotationConfigColumn... scxDaoColumnInfos) {
        return Arrays.stream(scxDaoColumnInfos).filter(field -> field.javaFieldValue(entity) != null).toArray(AnnotationConfigColumn[]::new);
    }

    public static JDBCType getDataTypeByJavaType(Type type) {
        if (type instanceof Class<?> clazz) {
            //普通 class 直接创建 失败后回退到 ObjectTypeHandler
            var c = getDataTypeByJavaType0(clazz);
            if (c != null) {
                return c;
            }
        } else if (type instanceof JavaType javaType) {
            //JavaType 先尝试使用 getRawClass 进行创建 失败后回退到 ObjectTypeHandler
            var c = getDataTypeByJavaType0(javaType.getRawClass());
            if (c != null) {
                return c;
            }
        }
        return JSON;
    }

    private static JDBCType getDataTypeByJavaType0(Class<?> clazz) {
        if (isEnum(clazz)) {
            return VARCHAR;
        } else {
            return JDBCType.getByJavaType(clazz);
        }
    }

}

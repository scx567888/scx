package cool.scx.data.jdbc;

import cool.scx.data.FieldFilter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 列过滤器
 *
 * @author scx567888
 * @version 0.1.3
 */
public final class FieldFilterHelper {

    /**
     * 过滤
     *
     * @param tableInfo 带过滤的列表
     * @return 过滤后的列表
     */
    public static AnnotationConfigColumn[] filter(FieldFilter fieldFilter, AnnotationConfigTable tableInfo) {
        return fieldFilter.getFieldNames().size() == 0 ? switch (fieldFilter.getFilterMode()) {
            case INCLUDED -> new AnnotationConfigColumn[0];
            case EXCLUDED -> tableInfo.columns();
        } : switch (fieldFilter.getFilterMode()) {
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
        return fieldFilter.getExcludeIfFieldValueIsNull() ? excludeIfFieldValueIsNull(entity, filter(fieldFilter, tableInfo)) : filter(fieldFilter, tableInfo);
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

}

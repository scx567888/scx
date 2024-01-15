package cool.scx.data.jdbc;

import cool.scx.data.FieldFilter;
import cool.scx.jdbc.ColumnMapping;
import cool.scx.jdbc.mapping.Table;

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
    public static ColumnMapping[] filter(FieldFilter fieldFilter, Table<? extends ColumnMapping> tableInfo) {
        return fieldFilter.getFieldNames().size() == 0 ? switch (fieldFilter.getFilterMode()) {
            case INCLUDED -> new ColumnMapping[0];
            case EXCLUDED -> tableInfo.columns();
        } : switch (fieldFilter.getFilterMode()) {
            case INCLUDED -> {
                var list = new ArrayList<ColumnMapping>();
                for (var fieldName : fieldFilter.getFieldNames()) {
                    list.add(tableInfo.getColumn(fieldName));
                }
                yield list.toArray(ColumnMapping[]::new);
            }
            case EXCLUDED -> {
                var objects = new ArrayList<>(Arrays.asList(tableInfo.columns()));
                for (var fieldName : fieldFilter.getFieldNames()) {
                    objects.remove(tableInfo.getColumn(fieldName));
                }
                yield objects.toArray(ColumnMapping[]::new);
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
    public static ColumnMapping[] filter(FieldFilter fieldFilter, Object entity, Table<? extends ColumnMapping> tableInfo) {
        return fieldFilter.getExcludeIfFieldValueIsNull() ? excludeIfFieldValueIsNull(entity, filter(fieldFilter, tableInfo)) : filter(fieldFilter, tableInfo);
    }

    /**
     * 过滤空值
     *
     * @param entity            e
     * @param scxDaoColumnInfos s
     * @return e
     */
    private static ColumnMapping[] excludeIfFieldValueIsNull(Object entity, ColumnMapping... scxDaoColumnInfos) {
        return Arrays.stream(scxDaoColumnInfos).filter(field -> field.javaFieldValue(entity) != null).toArray(ColumnMapping[]::new);
    }

}

package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.mapping.AnnotationConfigColumn;
import cool.scx.data.jdbc.mapping.AnnotationConfigTable;

import java.util.ArrayList;

import static java.util.Collections.addAll;

class Helper {

    /// 根据 字段策略过滤 可以插入的列
    public static AnnotationConfigColumn[] filterByFieldPolicy(FieldPolicy fieldPolicy, AnnotationConfigTable table, Object entity) {
        //1, 先根据 黑白名单进行过滤
        var columns = filterByFieldPolicy(fieldPolicy, table);
        //2, 根据 是否包含空值进行过滤 
        if (fieldPolicy.getIgnoreNullValue()) {
            columns = filterByFieldValueIsNull(entity, columns);
        }
        return columns;
    }

    /// 根据 字段策略过滤 可以插入的列
    public static AnnotationConfigColumn[] filterByFieldPolicy(FieldPolicy fieldPolicy, AnnotationConfigTable table) {
        var filterMode = fieldPolicy.getFilterMode();
        //1, 先根据 黑白名单进行过滤
        return switch (filterMode) {
            case INCLUDED -> filterByIncluded(fieldPolicy.getFieldNames(), table);
            case EXCLUDED -> filterByExcluded(fieldPolicy.getFieldNames(), table);
        };
    }

    /// 根据白名单进行过滤
    public static AnnotationConfigColumn[] filterByIncluded(String[] fieldNames, AnnotationConfigTable table) {
        //快速处理
        if (fieldNames.length == 0) {
            return new AnnotationConfigColumn[0];
        }

        var columns = new ArrayList<AnnotationConfigColumn>();
        for (var fieldName : fieldNames) {
            columns.add(table.getColumn(fieldName));
        }
        return columns.toArray(AnnotationConfigColumn[]::new);
    }

    /// 根据黑名单进行过滤
    public static AnnotationConfigColumn[] filterByExcluded(String[] fieldNames, AnnotationConfigTable table) {
        //快速处理
        if (fieldNames.length == 0) {
            return table.columns();
        }

        var allColumns = new ArrayList<AnnotationConfigColumn>();
        addAll(allColumns, table.columns());

        for (var fieldName : fieldNames) {
            allColumns.remove(table.getColumn(fieldName));
        }
        return allColumns.toArray(AnnotationConfigColumn[]::new);
    }

    /// 根据是否空值进行过滤
    private static AnnotationConfigColumn[] filterByFieldValueIsNull(Object entity, AnnotationConfigColumn... columns) {
        // 快速处理
        if (entity == null) {
            return new AnnotationConfigColumn[0];
        }
        var result = new ArrayList<AnnotationConfigColumn>();
        for (var column : columns) {
            if (column.javaFieldValue(entity) != null) {
                result.add(column);
            }
        }
        return result.toArray(AnnotationConfigColumn[]::new);
    }


    /// 提取值
    public static Object[] extractValues(AnnotationConfigColumn[] column, Object entity) {
        var result = new Object[column.length];
        if (entity == null) {
            return result;
        }
        for (var i = 0; i < column.length; i = i + 1) {
            result[i] = column[i].javaFieldValue(entity);
        }
        return result;
    }
    
}

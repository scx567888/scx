package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.field_policy.AssignField;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.field_policy.VirtualField;
import cool.scx.data.jdbc.mapping.AnnotationConfigColumn;
import cool.scx.data.jdbc.mapping.AnnotationConfigTable;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Column;

import java.util.ArrayList;
import java.util.Map;

import static java.util.Collections.addAll;

class SQLBuilderHelper {

    /// 根据 字段策略过滤 可以插入的列
    public static AnnotationConfigColumn[] filterByUpdateFieldPolicy(FieldPolicy fieldPolicy, AnnotationConfigTable table, Object entity) {
        //1, 先根据 黑白名单进行过滤
        var columns = filterByFilterMode(fieldPolicy, table);

        //2, 再根据 fieldExpressions 过滤
        var assignFields = fieldPolicy.getAssignFields();
        columns = filterByAssignFields(assignFields, table, columns);

        //3, 根据 是否包含空值进行过滤 
        var globalIgnoreNull = fieldPolicy.getIgnoreNull();
        var ignoreNulls = fieldPolicy.getIgnoreNulls();
        columns = filterByFieldValueIsNull(entity, globalIgnoreNull, ignoreNulls, columns);
        return columns;
    }

    /// 根据 字段策略过滤 可以插入的列 (注意在 fieldExpressions 中存在的 fieldName 也会被移除)
    public static AnnotationConfigColumn[] filterByUpdateFieldPolicy(FieldPolicy fieldPolicy, AnnotationConfigTable table) {
        //1, 先根据 黑白名单进行过滤
        var columns = filterByFilterMode(fieldPolicy, table);

        //2, 再根据 fieldExpressions 过滤
        var assignFields = fieldPolicy.getAssignFields();
        return filterByAssignFields(assignFields, table, columns);
    }

    /// 根据 字段策略过滤 可以插入的列 (注意在 fieldExpressions 中存在的 fieldName 也会被移除)
    public static AnnotationConfigColumn[] filterByQueryFieldPolicy(FieldPolicy fieldPolicy, AnnotationConfigTable table) {
        //1, 先根据 黑白名单进行过滤
        var columns = filterByFilterMode(fieldPolicy, table);

        //2, 再根据 virtualFields 过滤
        var virtualFields = fieldPolicy.getVirtualFields();
        return filterByVirtualFields(virtualFields, table, columns);
    }

    public static AnnotationConfigColumn[] filterByVirtualFields(VirtualField[] virtualFields, AnnotationConfigTable table, AnnotationConfigColumn... columns) {
        // 快速判断
        if (virtualFields.length == 0) {
            return columns;
        }

        var result = new ArrayList<AnnotationConfigColumn>();
        addAll(result, columns);
        //表达式中存在的就不应该 存在了
        for (var fieldName : virtualFields) {
            result.remove(table.getColumn(fieldName.virtualFieldName()));
        }
        return result.toArray(AnnotationConfigColumn[]::new);
    }

    public static AnnotationConfigColumn[] filterByFilterMode(FieldPolicy fieldPolicy, AnnotationConfigTable table) {
        var filterMode = fieldPolicy.getFilterMode();
        return switch (filterMode) {
            case INCLUDED -> filterByIncluded(fieldPolicy.getFieldNames(), table);
            case EXCLUDED -> filterByExcluded(fieldPolicy.getFieldNames(), table);
        };
    }

    /// 根据白名单进行过滤  (注意在 fieldExpressions 中存在的 fieldName 也会被移除)
    public static AnnotationConfigColumn[] filterByIncluded(String[] fieldNames, AnnotationConfigTable table) {
        //快速处理
        if (fieldNames.length == 0) {
            return new AnnotationConfigColumn[0];
        }

        var columns = new ArrayList<AnnotationConfigColumn>();
        for (var fieldName : fieldNames) {
            var column = table.getColumn(fieldName);
            if (column != null) {
                columns.add(column);
            }
        }
        return columns.toArray(AnnotationConfigColumn[]::new);
    }

    /// 根据黑名单进行过滤  (注意在 fieldExpressions 中存在的 fieldName 也会被移除)
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
    private static AnnotationConfigColumn[] filterByFieldValueIsNull(Object entity, boolean globalIgnoreNull, Map<String, Boolean> ignoreNulls, AnnotationConfigColumn... columns) {
        //快速方法

        if (entity == null && globalIgnoreNull && ignoreNulls.isEmpty()) {
            return new AnnotationConfigColumn[0];
        }

        if (!globalIgnoreNull && ignoreNulls.isEmpty()) {
            return columns;
        }

        // 正常过滤
        var result = new ArrayList<AnnotationConfigColumn>();
        for (var column : columns) {
            var fieldName = column.javaField().name();
            //判断是否需要忽略这个字段
            var ignoreNull = ignoreNulls.getOrDefault(fieldName, globalIgnoreNull);
            var value = (entity != null) ? column.javaFieldValue(entity) : null;

            // 若字段不忽略 null，或者字段值本身不为 null，则保留
            if (!ignoreNull || value != null) {
                result.add(column);
            }
        }
        return result.toArray(AnnotationConfigColumn[]::new);
    }

    public static AnnotationConfigColumn[] filterByAssignFields(AssignField[] assignFields, AnnotationConfigTable table, AnnotationConfigColumn... columns) {
        // 快速判断
        if (assignFields.length == 0) {
            return columns;
        }

        var result = new ArrayList<AnnotationConfigColumn>();
        addAll(result, columns);
        //表达式中存在的就不应该 存在了
        for (var fieldName : assignFields) {
            result.remove(table.getColumn(fieldName.fieldName()));
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

    public static String joinWithQuoteIdentifier(Object[] values, Dialect dialect) {
        var isFirst = true;
        var sb = new StringBuilder();
        for (var value : values) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
            }
            if (value instanceof Column c) {
                sb.append(dialect.quoteIdentifier(c.name()));
            } else {
                sb.append(value.toString());
            }
        }
        return sb.toString();
    }

}

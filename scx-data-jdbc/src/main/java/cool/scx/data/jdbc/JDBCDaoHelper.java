package cool.scx.data.jdbc;

import com.fasterxml.jackson.databind.JavaType;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.parser.JDBCDaoColumnNameParser;
import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static cool.scx.common.util.ClassUtils.isEnum;
import static cool.scx.jdbc.JDBCType.JSON;
import static cool.scx.jdbc.JDBCType.VARCHAR;

/// 列过滤器
///
/// @author scx567888
/// @version 0.0.1
public final class JDBCDaoHelper {

    public static String[] getVirtualColumns(FieldPolicy fieldFilter, Dialect dialect) {
        var fieldExpressions = fieldFilter.getFieldExpressions();
        var virtualColumns = new String[fieldExpressions.length];
        for (int i = 0; i < fieldExpressions.length; i++) {
            var fieldExpression = fieldExpressions[i];
            virtualColumns[i] = fieldExpression.expression() + " AS " + dialect.quoteIdentifier(fieldExpression.fieldName());
        }
        return virtualColumns;
    }

    public static String[] createUpdateSetExpressionsColumns(FieldPolicy fieldFilter,  JDBCDaoColumnNameParser columnNameParser) {
        var fieldExpressions = fieldFilter.getFieldExpressions();
        var result = new String[fieldExpressions.length];
        for (var i = 0; i < fieldExpressions.length; i = i + 1) {
            var fieldExpression = fieldExpressions[i];
            result[i] = columnNameParser.parseColumnName(fieldExpression.fieldName(),false)  + " = " + fieldExpression.expression();
        }
        return result;
    }

    //todo 是否需要 调用方言 或者转成列名
    public static String[] createInsertExpressionsColumns(FieldPolicy fieldFilter,JDBCDaoColumnNameParser parser) {
        var fieldExpressions = fieldFilter.getFieldExpressions();
        var result = new String[fieldExpressions.length];
        for (var i = 0; i < fieldExpressions.length; i = i + 1) {
            result[i] = parser.parseColumnName(fieldExpressions[i].fieldName(),false);
        }
        return result;
    }

    public static String[] createInsertExpressionsValue(FieldPolicy fieldFilter) {
        var fieldExpressions = fieldFilter.getFieldExpressions();
        var result = new String[fieldExpressions.length];
        for (var i = 0; i < fieldExpressions.length; i = i + 1) {
            result[i] = fieldExpressions[i].expression();
        }
        return result;
    }

    /// 过滤
    ///
    /// @param tableInfo 带过滤的列表
    /// @return 过滤后的列表
    public static AnnotationConfigColumn[] filter(FieldPolicy fieldFilter, AnnotationConfigTable tableInfo) {
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

    public static Column[] filter(FieldPolicy fieldFilter, Table tableInfo) {
        return fieldFilter.getFieldNames().length == 0 ?
                switch (fieldFilter.getFilterMode()) {
                    case INCLUDED -> new Column[0];
                    case EXCLUDED -> tableInfo.columns();
                } :
                switch (fieldFilter.getFilterMode()) {
                    case INCLUDED -> {
                        var list = new ArrayList<Column>();
                        for (var fieldName : fieldFilter.getFieldNames()) {
                            list.add(tableInfo.getColumn(fieldName));
                        }
                        yield list.toArray(Column[]::new);
                    }
                    case EXCLUDED -> {
                        var objects = new ArrayList<>(Arrays.asList(tableInfo.columns()));
                        for (var fieldName : fieldFilter.getFieldNames()) {
                            objects.remove(tableInfo.getColumn(fieldName));
                        }
                        yield objects.toArray(Column[]::new);
                    }
                };
    }

    /// 过滤
    ///
    /// @param entity    a
    /// @param tableInfo 带过滤的列表
    /// @return 过滤后的列表
    public static AnnotationConfigColumn[] filter(FieldPolicy fieldFilter, Object entity, AnnotationConfigTable tableInfo) {
        return fieldFilter.getIgnoreNullValue() ? excludeIfFieldValueIsNull(entity, filter(fieldFilter, tableInfo)) : filter(fieldFilter, tableInfo);
    }

    public static Column[] filter(FieldPolicy fieldFilter, Map<String, Object> entity, Table tableInfo) {
        return fieldFilter.getIgnoreNullValue() ? excludeIfFieldValueIsNull(entity, filter(fieldFilter, tableInfo)) : filter(fieldFilter, tableInfo);
    }

    /// 过滤空值
    ///
    /// @param entity            e
    /// @param scxDaoColumnInfos s
    /// @return e
    private static AnnotationConfigColumn[] excludeIfFieldValueIsNull(Object entity, AnnotationConfigColumn... scxDaoColumnInfos) {
        return Arrays.stream(scxDaoColumnInfos).filter(field ->
                entity != null && field.javaFieldValue(entity) != null
        ).toArray(AnnotationConfigColumn[]::new);
    }

    private static Column[] excludeIfFieldValueIsNull(Map<String, Object> entity, Column... scxDaoColumnInfos) {
        return Arrays.stream(scxDaoColumnInfos).filter(field -> entity.get(field.name()) != null).toArray(Column[]::new);
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

    public static String[] createUpdateSetColumns(Column[] columns, Dialect dialect) {
        var result = new String[columns.length];
        for (var i = 0; i < columns.length; i = i + 1) {
            result[i] = dialect.quoteIdentifier(columns[i].name()) + " = ?";
        }
        return result;
    }

    public static String[] createInsertValues(Column[] columns) {
        var result = new String[columns.length];
        for (var i = 0; i < result.length; i = i + 1) {
            result[i] = "?";
        }
        return result;
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

    /// 提取值
    public static Object[] extractValues(Column[] column, Map<String, Object> entity) {
        var result = new Object[column.length];
        if (entity == null) {
            return result;
        }
        for (var i = 0; i < column.length; i = i + 1) {
            result[i] = entity.get(column[i].name());
        }
        return result;
    }

    private static JDBCType getDataTypeByJavaType0(Class<?> clazz) {
        if (isEnum(clazz)) {
            return VARCHAR;
        } else {
            return JDBCType.getByJavaType(clazz);
        }
    }

}

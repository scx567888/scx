package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.mapping.AnnotationConfigTable;
import cool.scx.data.jdbc.parser.JDBCColumnNameParser;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.sql.SQL;

import java.util.ArrayList;
import java.util.Collection;

import static cool.scx.common.util.ArrayUtils.tryConcat;
import static cool.scx.common.util.ArrayUtils.tryConcatAny;
import static cool.scx.data.jdbc.sql_builder.SQLBuilder.joinWithQuoteIdentifier;
import static cool.scx.data.jdbc.sql_builder.SQLBuilderHelper.extractValues;
import static cool.scx.data.jdbc.sql_builder.SQLBuilderHelper.filterByUpdateFieldPolicy;
import static cool.scx.jdbc.sql.SQL.sql;

public class InsertSQLBuilder {

    private final AnnotationConfigTable table;
    private final Dialect dialect;
    private final JDBCColumnNameParser columnNameParser;

    public InsertSQLBuilder(AnnotationConfigTable table, Dialect dialect, JDBCColumnNameParser columnNameParser) {
        this.table = table;
        this.dialect = dialect;
        this.columnNameParser = columnNameParser;
    }

    public static String[] createInsertExpressionsColumns(FieldPolicy fieldPolicy, JDBCColumnNameParser parser) {
        var fieldExpressions = fieldPolicy.getExpressions();
        var result = new String[fieldExpressions.length];
        int i = 0;
        for (var fieldExpression : fieldExpressions) {
            result[i] = parser.parseColumnName(fieldExpression.fieldName(), false);
            i = i + 1;
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

    public static String[] createInsertExpressionsValue(FieldPolicy fieldPolicy) {
        var fieldExpressions = fieldPolicy.getExpressions();
        var result = new String[fieldExpressions.length];
        int i = 0;
        for (var fieldExpression : fieldExpressions) {
            result[i] = fieldExpression.expression();
            i = i + 1;
        }
        return result;
    }

    public SQL buildInsertSQL(Object entity, FieldPolicy fieldPolicy) {
        //1, 根据 字段策略过滤 可以插入的列
        var insertColumns = filterByUpdateFieldPolicy(fieldPolicy, table, entity);
        //2, 根据 字段策略 创建插入的表达式列
        var insertExpressionsColumns = createInsertExpressionsColumns(fieldPolicy, columnNameParser);
        //3, 创建 插入值 其实都是 '?' 
        var insertValues = createInsertValues(insertColumns);
        //4, 创建 插入表达式
        var insertExpressionsValue = createInsertExpressionsValue(fieldPolicy);
        //5, 拼接最终的 插入列
        var finalInsertColumns = tryConcatAny(insertColumns, (Object[]) insertExpressionsColumns);
        //6, 拼接最终的 插入值
        var finalInsertValues = tryConcat(insertValues, insertExpressionsValue);
        //7, 创建 SQL 语句字符串
        var sql = GetInsertSQL(finalInsertColumns, finalInsertValues);
        //8, 提取 entity 中的值作为 SQL 参数
        var params = extractValues(insertColumns, entity);
        return sql(sql, params);
    }

    public SQL buildInsertBatchSQL(Collection<?> entityList, FieldPolicy fieldPolicy) {
        //1, 根据 字段策略过滤 可以插入的列
        var insertColumns = filterByUpdateFieldPolicy(fieldPolicy, table);
        //2, 根据 字段策略 创建插入的表达式列
        var insertExpressionsColumns = createInsertExpressionsColumns(fieldPolicy, columnNameParser);
        //3, 创建 插入值 其实都是 '?' 
        var insertValues = createInsertValues(insertColumns);
        //4, 创建 插入表达式
        var insertExpressionsValue = createInsertExpressionsValue(fieldPolicy);
        //5, 拼接最终的 插入列
        var finalInsertColumns = tryConcatAny(insertColumns, (Object[]) insertExpressionsColumns);
        //6, 拼接最终的 插入值
        var finalInsertValues = tryConcat(insertValues, insertExpressionsValue);
        //7, 创建 SQL 语句字符串
        var sql = GetInsertSQL(finalInsertColumns, finalInsertValues);
        //8, 提取 entity 中的值作为 SQL 参数
        var batchParams = new ArrayList<Object[]>(entityList.size());
        for (var entity : entityList) {
            batchParams.add(extractValues(insertColumns, entity));
        }
        return sql(sql, batchParams);
    }

    /// @param insertColumns 存储列名 如 (name, age)
    /// @param insertValues  存储 values 如 ('scx', 1)
    private String GetInsertSQL(Object[] insertColumns, String[] insertValues) {
        return "INSERT INTO " + getTableName() + " (" + getInsertColumns(insertColumns) + ") VALUES (" + String.join(", ", insertValues) + ")";
    }

    private String getTableName() {
        return dialect.quoteIdentifier(table.name());
    }

    private String getInsertColumns(Object[] insertColumns) {
        return joinWithQuoteIdentifier(insertColumns, dialect);
    }

}

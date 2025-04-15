package cool.scx.data.jdbc;

import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.parser.JDBCDaoColumnNameParser;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.sql.SQL;

import java.util.Collection;

import static cool.scx.common.util.ArrayUtils.tryConcat;
import static cool.scx.common.util.ArrayUtils.tryConcatAny;
import static cool.scx.data.jdbc.A.filterByFieldPolicy;
import static cool.scx.data.jdbc.DataJDBCHelper.*;
import static cool.scx.data.jdbc.DataJDBCHelper.extractValues;
import static cool.scx.jdbc.sql.SQL.sql;
import static cool.scx.jdbc.sql.SQLBuilder.Insert;

public class InsertSQLBuilder {
    
    private final AnnotationConfigTable table;
    private final Dialect dialect;
    private final JDBCDaoColumnNameParser columnNameParser;

    public InsertSQLBuilder(AnnotationConfigTable table, Dialect dialect, JDBCDaoColumnNameParser columnNameParser) {
        this.table = table;
        this.dialect = dialect;
        this.columnNameParser = columnNameParser;
    }

    public SQL buildInsertSQL(Object entity, FieldPolicy fieldPolicy) {
        //1, 根据 字段策略过滤 可以插入的列
        var insertColumns = filterByFieldPolicy(fieldPolicy, table, entity);
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
        var sql = Insert(table, finalInsertColumns)
                .Values(finalInsertValues)
                .GetSQL(dialect);
        //8, 提取 entity 中的值作为 SQL 参数
        var params = extractValues(insertColumns, entity);
        return sql(sql, params);
    }


    public SQL buildInsertBatchSQL(Collection<?> entityList, FieldPolicy fieldPolicy) {
        //1, 根据 字段策略过滤 可以插入的列
        var insertColumns = filterByFieldPolicy(fieldPolicy, table);
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
        var sql = Insert(table, finalInsertColumns)
                .Values(finalInsertValues)
                .GetSQL(dialect);
        //8, 提取 entity 中的值作为 SQL 参数
        var batchParams = new Object[entityList.size()];
        var i = 0;
        for (var entity : entityList) {
            batchParams[i] = extractValues(insertColumns, entity);
            i = i + 1;
        }
        return sql(sql, batchParams);
    }
    
}

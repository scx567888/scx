package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.mapping.AnnotationConfigTable;
import cool.scx.data.jdbc.parser.JDBCColumnNameParser;
import cool.scx.data.jdbc.parser.JDBCOrderByParser;
import cool.scx.data.jdbc.parser.JDBCWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.sql.SQL;

import static cool.scx.common.util.ArrayUtils.tryConcat;
import static cool.scx.common.util.StringUtils.notEmpty;
import static cool.scx.data.jdbc.sql_builder.SQLBuilderHelper.extractValues;
import static cool.scx.data.jdbc.sql_builder.SQLBuilderHelper.filterByUpdateFieldPolicy;
import static cool.scx.jdbc.sql.SQL.sql;

public class UpdateSQLBuilder {

    private final AnnotationConfigTable table;
    private final Dialect dialect;
    private final JDBCColumnNameParser columnNameParser;
    private final JDBCWhereParser whereParser;
    private final JDBCOrderByParser orderByParser;

    public UpdateSQLBuilder(AnnotationConfigTable table, Dialect dialect, JDBCColumnNameParser columnNameParser, JDBCWhereParser whereParser, JDBCOrderByParser orderByParser) {
        this.table = table;
        this.dialect = dialect;
        this.columnNameParser = columnNameParser;
        this.whereParser = whereParser;
        this.orderByParser = orderByParser;
    }

    public static String[] createUpdateSetClauses(Column[] columns, Dialect dialect) {
        var result = new String[columns.length];
        for (var i = 0; i < columns.length; i = i + 1) {
            result[i] = dialect.quoteIdentifier(columns[i].name()) + " = ?";
        }
        return result;
    }

    public static String[] createUpdateSetExpressionsClauses(FieldPolicy fieldPolicy, JDBCColumnNameParser columnNameParser) {
        var assignFields = fieldPolicy.getAssignFields();
        var result = new String[assignFields.length];
        var i = 0;
        for (var entry : assignFields) {
            var fieldName = entry.fieldName();
            var expression = entry.expression();
            result[i] = columnNameParser.parseColumnName(fieldName, false) + " = " + expression;
            i = i + 1;
        }
        return result;
    }

    public SQL buildUpdateSQL(Object entity, FieldPolicy updateFilter, Query query) {
        if (query.getWhere() == null) {
            throw new IllegalArgumentException("更新数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        //1, 过滤需要更新的列
        var updateSetColumns = filterByUpdateFieldPolicy(updateFilter, table, entity);
        //2, 创建 set 子句 其实都是 '?'
        var updateSetClauses = createUpdateSetClauses(updateSetColumns, dialect);
        //3, 创建 表达式 set 子句
        var updateSetExpressionsColumns = createUpdateSetExpressionsClauses(updateFilter, columnNameParser);
        //4, 创建最终的 set 子句
        var finalUpdateSetClauses = tryConcat(updateSetClauses, updateSetExpressionsColumns);
        //5, 创建 where 子句
        var whereClause = whereParser.parse(query.getWhere());
        //6, 创建 orderBy 子句
        var orderByClauses = orderByParser.parse(query.getOrderBys());
        //7, 创建 SQL
        var sql = GetUpdateSQL(finalUpdateSetClauses, whereClause.expression(), orderByClauses, query.getLimit());
        //8, 提取 entity 参数
        var entityParams = extractValues(updateSetColumns, entity);
        //9, 拼接参数 
        var finalParams = tryConcat(entityParams, whereClause.params());
        return sql(sql, finalParams);
    }

    /// @param updateSetClauses 存储子句 如 (name = 'scx', age = 18)
    /// @param whereClause      存储子句 如 (name = 'scx', age > 18)
    /// @param orderByClauses   存储子句 如 (name desc, age asc)
    /// @param limit            Limit
    private String GetUpdateSQL(String[] updateSetClauses, String whereClause, String[] orderByClauses, Long limit) {
        if (updateSetClauses.length == 0) {
            throw new IllegalArgumentException("Set 子句错误 : 待更新的数据列 不能为空 !!!");
        }
        var sql = "UPDATE " + getTableName() + " SET " + String.join(", ", updateSetClauses) + getWhereClause(whereClause) + getOrderByClause(orderByClauses);
        // 更新时 limit 不能有 offset (偏移量)
        return dialect.getLimitSQL(sql, null, limit);
    }

    private String getTableName() {
        return dialect.quoteIdentifier(table.name());
    }

    private String getWhereClause(String whereClause) {
        return notEmpty(whereClause) ? " WHERE " + whereClause : "";
    }

    private String getOrderByClause(String[] orderByClauses) {
        return orderByClauses != null && orderByClauses.length != 0 ? " ORDER BY " + String.join(", ", orderByClauses) : "";
    }

}

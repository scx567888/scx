package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.jdbc.parser.JDBCOrderByParser;
import cool.scx.data.jdbc.parser.JDBCWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.sql.SQL;

import static cool.scx.common.util.StringUtils.notEmpty;
import static cool.scx.jdbc.sql.SQL.sql;

public class DeleteSQLBuilder {

    private final Table table;
    private final Dialect dialect;
    private final JDBCWhereParser whereParser;
    private final JDBCOrderByParser orderByParser;

    public DeleteSQLBuilder(Table table, Dialect dialect, JDBCWhereParser whereParser, JDBCOrderByParser orderByParser) {
        this.table = table;
        this.dialect = dialect;
        this.whereParser = whereParser;
        this.orderByParser = orderByParser;
    }

    public SQL buildDeleteSQL(Query query) {
        var whereClause = whereParser.parse(query.getWhere());
        var orderByClauses = orderByParser.parse(query.getOrderBys());
        var sql = GetDeleteSQL(whereClause.expression(), orderByClauses, query.getLimit());
        return sql(sql, whereClause.params());
    }

    public String GetDeleteSQL(String whereClause, String[] orderByClauses, Long limit) {
        if (whereClause == null || whereClause.isEmpty()) {
            throw new IllegalArgumentException("删除数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var sql = "DELETE FROM " + getTableName() + getWhereClause(whereClause) + getOrderByClause(orderByClauses);
        // 删除时 limit 不能有 offset (偏移量)
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

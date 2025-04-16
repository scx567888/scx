package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.jdbc.parser.JDBCDaoOrderByParser;
import cool.scx.data.jdbc.parser.JDBCDaoWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.sql.SQL;

import static cool.scx.jdbc.sql.SQL.sql;
import static cool.scx.jdbc.sql.SQLBuilder.Delete;

public class DeleteSQLBuilder {

    private final Table table;
    private final Dialect dialect;
    private final JDBCDaoWhereParser whereParser;
    private final JDBCDaoOrderByParser orderByParser;

    public DeleteSQLBuilder(Table table, Dialect dialect, JDBCDaoWhereParser whereParser, JDBCDaoOrderByParser orderByParser) {
        this.table = table;
        this.dialect = dialect;
        this.whereParser = whereParser;
        this.orderByParser = orderByParser;
    }

    public SQL buildDeleteSQL(Query query) {
        if (query.getWhere().length == 0) {
            throw new IllegalArgumentException("删除数据时 必须指定 删除条件 或 自定义的 where 语句 !!!");
        }
        var whereClause = whereParser.parse(query.getWhere());
        var orderByClauses = orderByParser.parse(query.getOrderBy());
        var sql = Delete(table)
                .Where(whereClause.whereClause())
                .OrderBy(orderByClauses)
                .Limit(null, query.getLimit())
                .GetSQL(dialect);
        return sql(sql, whereClause.params());
    }

}

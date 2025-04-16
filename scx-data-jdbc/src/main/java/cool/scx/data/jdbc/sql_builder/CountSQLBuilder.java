package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.jdbc.parser.JDBCDaoGroupByParser;
import cool.scx.data.jdbc.parser.JDBCDaoWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.sql.SQL;

import static cool.scx.jdbc.sql.SQL.sql;
import static cool.scx.jdbc.sql.SQLBuilder.Select;

public class CountSQLBuilder {

    private final JDBCDaoWhereParser whereParser;
    private final JDBCDaoGroupByParser groupByParser;
    private final Table table;
    private final Dialect dialect;

    public CountSQLBuilder(Table table, Dialect dialect, JDBCDaoWhereParser whereParser, JDBCDaoGroupByParser groupByParser) {
        this.whereParser = whereParser;
        this.groupByParser = groupByParser;
        this.table = table;
        this.dialect = dialect;
    }

    public SQL buildCountSQL(Query query) {
        var whereClause = whereParser.parse(query.getWhere());
        var groupByColumns = groupByParser.parse(query.getGroupBy());
        var sql = Select("COUNT(*) AS count")
                .From(table)
                .Where(whereClause.whereClause())
                .GroupBy(groupByColumns)
                .GetSQL(dialect);
        return sql(sql, whereClause.params());
    }

}

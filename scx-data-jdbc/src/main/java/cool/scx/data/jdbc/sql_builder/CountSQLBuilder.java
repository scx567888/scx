package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.jdbc.parser.JDBCGroupByParser;
import cool.scx.data.jdbc.parser.JDBCWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.sql.SQL;

import static cool.scx.jdbc.sql.SQL.sql;
import static cool.scx.jdbc.sql.SQLBuilder.Select;

public class CountSQLBuilder {

    private final JDBCWhereParser whereParser;
    private final JDBCGroupByParser groupByParser;
    private final Table table;
    private final Dialect dialect;

    public CountSQLBuilder(Table table, Dialect dialect, JDBCWhereParser whereParser, JDBCGroupByParser groupByParser) {
        this.whereParser = whereParser;
        this.groupByParser = groupByParser;
        this.table = table;
        this.dialect = dialect;
    }

    public SQL buildCountSQL(Query query) {
        var whereClause = whereParser.parse(query.getWhere());
//        var groupByColumns = groupByParser.parse(query.getGroupBy()); todo
        var sql = Select("COUNT(*) AS count")
                .From(table)
                .Where(whereClause.whereClause())
//                .GroupBy(groupByColumns) todo
                .GetSQL(dialect);
        return sql(sql, whereClause.params());
    }

}

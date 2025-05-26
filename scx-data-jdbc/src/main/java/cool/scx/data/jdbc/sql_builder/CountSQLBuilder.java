package cool.scx.data.jdbc.sql_builder;

import cool.scx.data.jdbc.parser.JDBCWhereParser;
import cool.scx.data.query.Query;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.sql.SQL;

import static cool.scx.common.util.StringUtils.notEmpty;
import static cool.scx.jdbc.sql.SQL.sql;

public class CountSQLBuilder {

    private final JDBCWhereParser whereParser;
    private final Table table;
    private final Dialect dialect;

    public CountSQLBuilder(Table table, Dialect dialect, JDBCWhereParser whereParser) {
        this.whereParser = whereParser;
        this.table = table;
        this.dialect = dialect;
    }

    public SQL buildCountSQL(Query query) {
        var whereClause = whereParser.parse(query.getWhere());
        var sql = GetCountSQL(whereClause.expression());
        return sql(sql, whereClause.params());
    }

    private String GetCountSQL(String whereClause) {
        return "SELECT COUNT(*) AS count FROM " + getTableName(dialect) + getWhereClause(whereClause);
    }

    private String getTableName(Dialect dialect) {
        return dialect.quoteIdentifier(table.name());
    }

    private String getWhereClause(String whereClause) {
        return notEmpty(whereClause) ? " WHERE " + whereClause : "";
    }

}

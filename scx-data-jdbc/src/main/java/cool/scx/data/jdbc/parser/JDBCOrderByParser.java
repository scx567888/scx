package cool.scx.data.jdbc.parser;

import cool.scx.data.query.OrderBy;
import cool.scx.data.query.parser.OrderByParser;

/// JDBCDaoOrderByParser
///
/// @author scx567888
/// @version 0.0.1
public class JDBCOrderByParser extends OrderByParser {

    private final JDBCColumnNameParser columnNameParser;

    public JDBCOrderByParser(JDBCColumnNameParser columnNameParser) {
        this.columnNameParser = columnNameParser;
    }

    @Override
    protected String[] parseOrderBy(OrderBy o) {
        var columnName = columnNameParser.parseColumnName(o);
        return new String[]{columnName + " " + o.orderByType().name()};
    }

}

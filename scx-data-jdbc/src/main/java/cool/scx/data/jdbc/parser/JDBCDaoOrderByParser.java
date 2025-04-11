package cool.scx.data.jdbc.parser;

import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.parser.OrderByParser;
import cool.scx.jdbc.dialect.Dialect;

/// JDBCDaoOrderByParser
///
/// @author scx567888
/// @version 0.0.1
public class JDBCDaoOrderByParser extends OrderByParser {

    private final JDBCDaoColumnNameParser columnNameParser;

    public JDBCDaoOrderByParser(JDBCDaoColumnNameParser columnNameParser) {
        this.columnNameParser = columnNameParser;
    }

    @Override
    protected String[] parseOrderBy(OrderBy o) {
        var columnName = columnNameParser.parseColumnName(o);
        return new String[]{columnName + " " + o.orderByType().name()};
    }

}

package cool.scx.data.jdbc.parser;

import cool.scx.data.query.GroupBy;
import cool.scx.data.query.parser.GroupByParser;

/// JDBCDaoGroupByParser
///
/// @author scx567888
/// @version 0.0.1
public class JDBCDaoGroupByParser extends GroupByParser {

    private final JDBCDaoColumnNameParser columnNameParser;

    public JDBCDaoGroupByParser(JDBCDaoColumnNameParser columnNameParser) {
        this.columnNameParser = columnNameParser;
    }

    @Override
    protected String[] parseGroupBy(GroupBy g) {
        var columnName = columnNameParser.parseColumnName(g);
        return new String[]{columnName};
    }

}

package cool.scx.data.jdbc.parser;

import cool.scx.data.aggregation.GroupBy;

/// JDBCDaoGroupByParser
///
/// @author scx567888
/// @version 0.0.1
public class JDBCGroupByParser {

    private final JDBCColumnNameParser columnNameParser;

    public JDBCGroupByParser(JDBCColumnNameParser columnNameParser) {
        this.columnNameParser = columnNameParser;
    }

    public String[] parseGroupBy(GroupBy g) {
        var columnName = columnNameParser.parseColumnName(g);
        return new String[]{columnName};
    }

}

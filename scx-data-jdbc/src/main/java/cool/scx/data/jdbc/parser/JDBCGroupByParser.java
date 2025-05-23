package cool.scx.data.jdbc.parser;

import cool.scx.data.aggregation.FieldGroupBy;

/// JDBCDaoGroupByParser
///
/// @author scx567888
/// @version 0.0.1
public class JDBCGroupByParser {

    private final JDBCColumnNameParser columnNameParser;

    public JDBCGroupByParser(JDBCColumnNameParser columnNameParser) {
        this.columnNameParser = columnNameParser;
    }

    public String parseGroupBy(FieldGroupBy g) {
        return columnNameParser.parseColumnName(g);
    }

}

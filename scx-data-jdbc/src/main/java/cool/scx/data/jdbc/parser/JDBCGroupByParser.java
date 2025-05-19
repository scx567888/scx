package cool.scx.data.jdbc.parser;

import cool.scx.data.aggregation_definition.GroupBy;

/// JDBCDaoGroupByParser todo 待完成
///
/// @author scx567888
/// @version 0.0.1
public class JDBCGroupByParser  {

    private final JDBCColumnNameParser columnNameParser;

    public JDBCGroupByParser(JDBCColumnNameParser columnNameParser) {
        this.columnNameParser = columnNameParser;
    }

//    @Override
    public String[] parseGroupBy(GroupBy g) {
        var columnName = columnNameParser.parseColumnName(g);
        return new String[]{columnName};
    }

}

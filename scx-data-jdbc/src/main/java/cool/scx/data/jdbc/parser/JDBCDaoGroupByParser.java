package cool.scx.data.jdbc.parser;

import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.query.GroupBy;
import cool.scx.data.query.parser.GroupByParser;
import cool.scx.jdbc.dialect.Dialect;

import static cool.scx.data.jdbc.parser.ColumnNameHelper.parseColumnName;

/// JDBCDaoGroupByParser
///
/// @author scx567888
/// @version 0.0.1
public class JDBCDaoGroupByParser extends GroupByParser {

    private final AnnotationConfigTable tableInfo;
    private final Dialect dialect;

    public JDBCDaoGroupByParser(AnnotationConfigTable tableInfo, Dialect dialect) {
        this.tableInfo = tableInfo;
        this.dialect = dialect;
    }

    @Override
    protected String[] parseGroupBy(GroupBy g) {
        var columnName = parseColumnName(tableInfo, g.name(), g.info().useJsonExtract(), g.info().useOriginalName());
        return new String[]{dialect.quoteIdentifier(columnName)};
    }

}

package cool.scx.data.jdbc.parser;

import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.parser.OrderByParser;
import cool.scx.jdbc.dialect.Dialect;

import static cool.scx.data.jdbc.parser.ColumnNameHelper.parseColumnName;

/// JDBCDaoOrderByParser
///
/// @author scx567888
/// @version 0.0.1
public class JDBCDaoOrderByParser extends OrderByParser {

    private final AnnotationConfigTable tableInfo;
    private final Dialect dialect;

    public JDBCDaoOrderByParser(AnnotationConfigTable tableInfo, Dialect dialect) {
        this.tableInfo = tableInfo;
        this.dialect = dialect;
    }

    @Override
    protected String[] parseOrderBy(OrderBy o) {
        var columnName = parseColumnName(tableInfo, o.name(), o.info().useJsonExtract(), o.info().useOriginalName());
        return new String[]{dialect.quoteIdentifier(columnName) + " " + o.orderByType().name()};
    }

}

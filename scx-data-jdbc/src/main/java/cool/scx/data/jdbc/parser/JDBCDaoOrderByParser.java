package cool.scx.data.jdbc.parser;

import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.parser.OrderByParser;

import static cool.scx.data.jdbc.parser.ColumnNameParser.parseColumnName;

public class JDBCDaoOrderByParser extends OrderByParser {

    private final AnnotationConfigTable tableInfo;

    public JDBCDaoOrderByParser(AnnotationConfigTable tableInfo) {
        this.tableInfo = tableInfo;
    }

    @Override
    protected String[] parseOrderBy(OrderBy o) {
        var columnName = parseColumnName(tableInfo, o.name(), o.info().useJsonExtract(), o.info().useOriginalName());
        return new String[]{columnName + " " + o.orderByType().name()};
    }

}

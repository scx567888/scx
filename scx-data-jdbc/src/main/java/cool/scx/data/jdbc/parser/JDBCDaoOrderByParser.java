package cool.scx.data.jdbc.parser;

import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.query.OrderByBody;
import cool.scx.data.query.parser.OrderByParser;

import static cool.scx.data.jdbc.parser.ColumnNameParser.parseColumnName;

public class JDBCDaoOrderByParser extends OrderByParser {

    private final AnnotationConfigTable tableInfo;

    public JDBCDaoOrderByParser(AnnotationConfigTable tableInfo) {
        this.tableInfo = tableInfo;
    }

    @Override
    protected String parseOrderByBody(OrderByBody body) {
        var columnName = parseColumnName(tableInfo, body.name(), body.info().useJsonExtract(), body.info().useOriginalName());
        return columnName + " " + body.orderByType().name();
    }

}

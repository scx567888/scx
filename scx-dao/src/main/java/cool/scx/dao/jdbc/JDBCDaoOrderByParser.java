package cool.scx.dao.jdbc;

import cool.scx.dao.AnnotationConfigTable;
import cool.scx.dao.query.OrderByBody;
import cool.scx.dao.query.parser.OrderByParser;

import static cool.scx.dao.jdbc.ColumnNameParser.parseColumnName;

class JDBCDaoOrderByParser extends OrderByParser {

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

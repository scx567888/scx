package cool.scx.dao.jdbc;

import cool.scx.dao.AnnotationConfigTable;
import cool.scx.dao.query.GroupByBody;
import cool.scx.dao.query.parser.GroupByParser;

import static cool.scx.dao.jdbc.ColumnNameParser.parseColumnName;

class JDBCDaoGroupByParser extends GroupByParser {

    private final AnnotationConfigTable tableInfo;

    public JDBCDaoGroupByParser(AnnotationConfigTable tableInfo) {
        this.tableInfo = tableInfo;
    }

    @Override
    public String parseGroupByBody(GroupByBody body) {
        return parseColumnName(tableInfo, body.name(), body.info().useJsonExtract(), body.info().useOriginalName());
    }

}

package cool.scx.data.jdbc.parser;

import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.query.GroupByBody;
import cool.scx.data.query.parser.GroupByParser;

import static cool.scx.data.jdbc.parser.ColumnNameParser.parseColumnName;

public class JDBCDaoGroupByParser extends GroupByParser {

    private final AnnotationConfigTable tableInfo;

    public JDBCDaoGroupByParser(AnnotationConfigTable tableInfo) {
        this.tableInfo = tableInfo;
    }

    @Override
    public String parseGroupByBody(GroupByBody body) {
        return parseColumnName(tableInfo, body.name(), body.info().useJsonExtract(), body.info().useOriginalName());
    }

}

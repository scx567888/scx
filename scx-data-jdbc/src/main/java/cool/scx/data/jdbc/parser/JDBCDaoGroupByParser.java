package cool.scx.data.jdbc.parser;

import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.query.GroupBy;
import cool.scx.data.query.parser.GroupByParser;

import static cool.scx.data.jdbc.parser.ColumnNameParser.parseColumnName;

public class JDBCDaoGroupByParser extends GroupByParser {

    private final AnnotationConfigTable tableInfo;

    public JDBCDaoGroupByParser(AnnotationConfigTable tableInfo) {
        this.tableInfo = tableInfo;
    }

    @Override
    protected String[] parseGroupBy(GroupBy g) {
        return new String[]{parseColumnName(tableInfo, g.name(), g.info().useJsonExtract(), g.info().useOriginalName())};
    }

}

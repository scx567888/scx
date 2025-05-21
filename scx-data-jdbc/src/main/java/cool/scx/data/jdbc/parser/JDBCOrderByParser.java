package cool.scx.data.jdbc.parser;

import cool.scx.data.query.OrderBy;

import java.util.ArrayList;

import static java.util.Collections.addAll;

/// JDBCDaoOrderByParser
///
/// @author scx567888
/// @version 0.0.1
public class JDBCOrderByParser {

    private final JDBCColumnNameParser columnNameParser;

    public JDBCOrderByParser(JDBCColumnNameParser columnNameParser) {
        this.columnNameParser = columnNameParser;
    }

    public String[] parse(OrderBy[] orderBys) {
        var list = new ArrayList<String>();
        for (var obj : orderBys) {
            var s = parseOrderBy(obj);
            list.add(s);
        }
        return list.toArray(String[]::new);
    }

    private String parseOrderBy(OrderBy o) {
        var columnName = columnNameParser.parseColumnName(o);
        return columnName + " " + o.orderByType().name();
    }

}

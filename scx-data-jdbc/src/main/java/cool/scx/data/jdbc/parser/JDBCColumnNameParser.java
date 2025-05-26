package cool.scx.data.jdbc.parser;

import cool.scx.data.aggregation.FieldGroupBy;
import cool.scx.data.query.Condition;
import cool.scx.data.query.OrderBy;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Table;

/// ColumnNameParser
///
/// @author scx567888
/// @version 0.0.1
public final class JDBCColumnNameParser {

    private final Table tableInfo;
    private final Dialect dialect;

    public JDBCColumnNameParser(Table tableInfo, Dialect dialect) {
        this.tableInfo = tableInfo;
        this.dialect = dialect;
    }

    public String parseColumnName(Condition w) {
        return parseColumnName(w.selector(), w.useExpression());
    }

    public String parseColumnName(FieldGroupBy g) {
        return parseColumnName(g.fieldName(), false);
    }

    public String parseColumnName(OrderBy o) {
        return parseColumnName(o.selector(), o.useExpression());
    }

    public String parseColumnName(String name, boolean useExpression) {
        // 这里就是普通的判断一下是否使用 原始名称即可
        if (useExpression) {
            //包裹表达式
            return "(" + name + ")";
        }
        var column = tableInfo.getColumn(name);
        if (column == null) {
            throw new IllegalArgumentException("在 Table : " + tableInfo.name() + " 中 , 未找到对应 name 为 : " + name + " 的列 !!!");
        } else {
            return dialect.quoteIdentifier(column.name());
        }
    }

}

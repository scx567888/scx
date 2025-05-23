package cool.scx.data.jdbc.parser;

import cool.scx.data.aggregation.FieldGroupBy;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.Where;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Table;

import static cool.scx.common.util.StringUtils.notBlank;

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

    public static ColumnNameAndFieldPath splitIntoColumnNameAndFieldPath(String name) {
        var charArray = name.toCharArray();
        var index = charArray.length;
        for (int i = 0; i < charArray.length; i = i + 1) {
            var c = charArray[i];
            if (c == '.' || c == '[') {
                index = i;
                break;
            }
        }
        var columnName = name.substring(0, index);
        var fieldPath = name.substring(index);
        return new ColumnNameAndFieldPath(columnName, fieldPath);
    }

    public String parseColumnName(Where w) {
        return parseColumnName(w.selector(), w.info().useJsonExtract(), w.info().useExpression());
    }

    public String parseColumnName(FieldGroupBy g) {
        return parseColumnName(g.fieldName(), g.info().useJsonExtract(), g.info().useExpression());
    }

    public String parseColumnName(OrderBy o) {
        return parseColumnName(o.selector(), o.info().useJsonExtract(), o.info().useExpression());
    }

    public String parseColumnName(String name, boolean useJsonExtract, boolean useExpression) {
        if (useJsonExtract) {
            var c = splitIntoColumnNameAndFieldPath(name);
            if (notBlank(c.columnName()) && notBlank(c.fieldPath())) {
                var jsonQueryColumnName = parseColumnName(c.columnName(), useExpression);
                return jsonQueryColumnName + " -> " + "'$" + c.fieldPath() + "'";
            } else {
                throw new IllegalArgumentException("使用 USE_JSON_EXTRACT 时, 查询名称不合法 !!! 字段名 : " + name);
            }
        }
        // 这里就是普通的判断一下是否使用 原始名称即可
        return parseColumnName(name, useExpression);
    }

    public String parseColumnName(String name, boolean useExpression) {
        // 这里就是普通的判断一下是否使用 原始名称即可
        if (useExpression) {
            return dialect.quoteIdentifier(name);
        }
        var column = tableInfo.getColumn(name);
        if (column == null) {
            throw new IllegalArgumentException("在 Table : " + tableInfo.name() + " 中 , 未找到对应 name 为 : " + name + " 的列 !!!");
        } else {
            return dialect.quoteIdentifier(column.name());
        }
    }

    public record ColumnNameAndFieldPath(String columnName, String fieldPath) {

    }

}

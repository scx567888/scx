package cool.scx.data.jdbc.parser;


import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;

import static cool.scx.common.util.StringUtils.notBlank;

public final class ColumnNameParser {

    public static String parseColumnName(Table tableInfo, String name, boolean useJsonExtract, boolean useOriginalName) {
        if (useJsonExtract) {
            var c = splitIntoColumnNameAndFieldPath(name);
            if (notBlank(c.columnName()) && notBlank(c.fieldPath())) {
                var jsonQueryColumnName = useOriginalName ? c.columnName() : tableInfo.getColumn(c.columnName()).name();
                return jsonQueryColumnName + " -> " + "'$" + c.fieldPath() + "'";
            } else {
                throw new IllegalArgumentException("使用 USE_JSON_EXTRACT 时, 查询名称不合法 !!! 字段名 : " + name);
            }
        } else {// 这里就是普通的判断一下是否使用 原始名称即可
            if (useOriginalName) {
                return name;
            }
            Column column = tableInfo.getColumn(name);
            if (column == null) {
                throw new IllegalArgumentException("在 Table : " + tableInfo.name() + " 中 , 未找到对应 name 为 : " + name + " 的列 !!!");
            } else {
                return column.name();
            }
        }
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

    public record ColumnNameAndFieldPath(String columnName, String fieldPath) {

    }

}

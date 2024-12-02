package cool.scx.jdbc.dialect;

import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.mapping.type.TypeDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cool.scx.common.util.StringUtils.notEmpty;

/**
 * DDLBuilder
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface DDLBuilder {

    /**
     * 获取建表语句
     *
     * @return s
     */
    default String getCreateTableDDL(Table table) {
        var s = new StringBuilder();
        s.append("CREATE TABLE ");
        if (notEmpty(table.schema())) {
            s.append(table.schema()).append(".");
        }
        s.append("`").append(table.name()).append("`").append("\n");
        s.append("(\n");

        // 创建子句
        var createDefinitionStr = getCreateDefinition(table).stream()
                .map(c -> "    " + c)
                .collect(Collectors.joining(",\n"));
        s.append(createDefinitionStr);

        s.append("\n);");
        return s.toString();
    }

    default List<String> getCreateDefinition(Table table) {
        var createDefinitions = new ArrayList<String>();
        createDefinitions.addAll(getColumnDefinitions(table.columns()));
        createDefinitions.addAll(getTableConstraint(table));
        return createDefinitions;
    }

    default List<String> getColumnDefinitions(Column[] columns) {
        var list = new ArrayList<String>();
        for (var column : columns) {
            list.add(getColumnDefinition(column));
        }
        return list;
    }

    default List<String> getTableConstraint(Table table) {
        return new ArrayList<>();
    }

    default String getColumnDefinition(Column column) {
        var s = new StringBuilder();
        s.append("`").append(column.name()).append("`").append(" ");// 列名
        var dataTypeDefinition = getDataTypeDefinition(column);
        if (dataTypeDefinition != null) {
            s.append(dataTypeDefinition).append(" ");
        }
        // 限制条件
        var columnConstraintStr = String.join(" ", getColumnConstraint(column));
        s.append(columnConstraintStr);
        return s.toString();
    }

    default String getDataTypeDefinition(Column column) {
        if (column.dataType() != null) {
            var _dataType = column.dataType();
            var _name = _dataType.name();
            // TypeDataType 做特殊处理
            if (_dataType instanceof TypeDataType m && m.jdbcType() != null) {
                _name = getDataTypeNameByJDBCType(m.jdbcType());
            }
            return getDataTypeDefinitionByName(_name, _dataType.length());
        }
        return defaultDateType();
    }

    /**
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    default List<String> getColumnConstraint(Column column) {
        return new ArrayList<>();
    }

    String getDataTypeNameByJDBCType(JDBCType dataType);

    default String getDataTypeDefinitionByName(String dataType, Integer length) {
        return length != null ? dataType + "(" + length + ")" : dataType;
    }

    /**
     * 默认值
     * todo 是否需要 ?
     *
     * @return 默认类型值
     */
    default String defaultDateType() {
        return null;
    }

    /**
     * todo 暂时只支持添加新字段 需要同时支持 删除或修改
     * 获取 AlertTableDDL
     *
     * @param needAdds  a
     * @param tableInfo a
     */
    default String getAlertTableDDL(Column[] needAdds, Table tableInfo) {
        var s = new StringBuilder();
        s.append("ALTER TABLE ");
        if (notEmpty(tableInfo.schema())) {
            s.append(tableInfo.schema()).append(".");
        }
        s.append("`").append(tableInfo.name()).append("`").append("\n");

        var columnDefinitionStr = getColumnDefinitions(needAdds).stream()
                .map(c -> "    ADD COLUMN " + c)
                .collect(Collectors.joining(",\n"));

        s.append(columnDefinitionStr);
        s.append("\n;");
        return s.toString();
    }

}

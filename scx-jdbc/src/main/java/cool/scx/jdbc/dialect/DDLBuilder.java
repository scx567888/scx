package cool.scx.jdbc.dialect;

import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.mapping.type.TypeColumn;
import cool.scx.jdbc.standard.StandardDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cool.scx.util.StringUtils.notEmpty;

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
        s.append(table.name()).append("\n");
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
        s.append(column.name()).append(" ");// 列名
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
            if (column.dataType().length() != null) {
                return column.dataType().name() + "(" + column.dataType().length() + ")";
            } else {
                return column.dataType().name();
            }
        } else {
            if (column instanceof TypeColumn m) {
                return getDataTypeDefinitionByClass(m.javaField().getType());
            } else {
                return defaultDateType();
            }
        }
    }

    /**
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    default List<String> getColumnConstraint(Column column) {
        return new ArrayList<>();
    }

    /**
     * 根据 class 获取对应的 SQLType 类型 如果没有则返回 JSON
     * todo 是否需要 ?
     *
     * @param javaType 需要获取的类型
     * @return a {@link String} object.
     */
    default String getDataTypeDefinitionByClass(Class<?> javaType) {
        var standardDataType = StandardDataType.getByJavaType(javaType);
        if (standardDataType == null) {
            if (javaType.isEnum()) {
                standardDataType = StandardDataType.VARCHAR;
            } else {
                standardDataType = StandardDataType.JSON;
            }
        }
        return getDataTypeDefinitionByStandardDataType(standardDataType);
    }

    String getDataTypeDefinitionByStandardDataType(StandardDataType dataType);

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
        s.append(tableInfo.name()).append("\n");

        var columnDefinitionStr = getColumnDefinitions(needAdds).stream()
                .map(c -> "    ADD COLUMN " + c)
                .collect(Collectors.joining(",\n"));

        s.append(columnDefinitionStr);
        s.append("\n;");
        return s.toString();
    }

}

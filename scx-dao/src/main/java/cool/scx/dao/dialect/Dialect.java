package cool.scx.dao.dialect;

import cool.scx.dao.ColumnMapping;
import cool.scx.sql.mapping.Column;
import cool.scx.sql.mapping.Table;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cool.scx.util.StringUtils.notEmpty;

public interface Dialect {

    /**
     * 是否可以处理
     *
     * @param dataSource 数据源
     * @return 是否可以处理
     */
    boolean canHandle(DataSource dataSource);

    /**
     * 是否可以处理
     *
     * @param driver 驱动
     * @return 是否可以处理
     */
    boolean canHandle(Driver driver);

    /**
     * 　获取最终的 SQL, 一般用于 Debug
     *
     * @param statement s
     * @return SQL 语句
     */
    String getFinalSQL(Statement statement);

    /**
     * 获取分页 SQL
     *
     * @param sql      原始 SQL
     * @param rowCount 行数
     * @param offset   偏移量
     * @return SQL 语句
     */
    String getLimitSQL(String sql, Long offset, Long rowCount);

    /**
     * 获取建表语句
     *
     * @return s
     */
    default String getCreateTableDDL(Table<?> tableInfo) {
        var s = new StringBuilder();
        s.append("CREATE TABLE ");
        if (notEmpty(tableInfo.schema())) {
            s.append(tableInfo.schema()).append(".");
        }
        s.append(tableInfo.name()).append("\n");
        s.append("(\n");

        // 创建子句
        var createDefinitionStr = getCreateDefinition(tableInfo).stream()
                .map(c -> "    " + c)
                .collect(Collectors.joining(",\n"));
        s.append(createDefinitionStr);

        s.append("\n);");
        return s.toString();
    }

    default List<String> getCreateDefinition(Table<?> table) {
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

    default List<String> getTableConstraint(Table<?> table) {
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
        if (column.typeName() != null) {
            if (column.columnSize() != null) {
                return column.typeName() + "(" + column.columnSize() + ")";
            } else {
                return column.typeName();
            }
        } else {
            if (column instanceof ColumnMapping m) {
                return getDataTypeDefinitionByClass(m.javaField().getType());
            } else {
                return defaultDateType();
            }
        }
    }

    List<String> getColumnConstraint(Column columns);

    /**
     * 根据 class 获取对应的 SQLType 类型 如果没有则返回 JSON
     *
     * @param javaType 需要获取的类型
     * @return a {@link String} object.
     */
    String getDataTypeDefinitionByClass(Class<?> javaType);

    /**
     * 默认值
     *
     * @return 默认类型值
     */
    default String defaultDateType() {
        return null;
    }

    /**
     * todo
     *
     * @param needAdds  a
     * @param tableInfo a
     */
    default String getAlertTableDDL(Column[] needAdds, Table<?> tableInfo) {
        var s = new StringBuilder();
        s.append("ALTER TABLE ");
        if (notEmpty(tableInfo.schema())) {
            s.append(tableInfo.schema()).append(".");
        }
        s.append(tableInfo.name()).append("`\n");

        var columnDefinitionStr = getColumnDefinitions(needAdds).stream()
                .map(c -> "    ADD COLUMN" + c)
                .collect(Collectors.joining(",\n"));

        s.append(columnDefinitionStr);
        s.append("\n;");
        return s.toString();
    }

}

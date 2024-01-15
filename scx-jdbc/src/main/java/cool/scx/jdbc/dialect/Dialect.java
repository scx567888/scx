package cool.scx.jdbc.dialect;

import cool.scx.jdbc.ColumnMapping;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.type_handler.TypeHandler;
import cool.scx.jdbc.type_handler.TypeHandlerSelector;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cool.scx.util.StringUtils.notEmpty;

public abstract class Dialect {

    protected final TypeHandlerSelector typeHandlerSelector = new TypeHandlerSelector();

    /**
     * 是否可以处理
     *
     * @param url 数据连接地址
     * @return 是否可以处理
     */
    public abstract boolean canHandle(String url);

    /**
     * 是否可以处理
     *
     * @param dataSource 数据源
     * @return 是否可以处理
     */
    public abstract boolean canHandle(DataSource dataSource);

    /**
     * 是否可以处理
     *
     * @param driver 驱动
     * @return 是否可以处理
     */
    public abstract boolean canHandle(Driver driver);

    /**
     * 　获取最终的 SQL, 一般用于 Debug
     *
     * @param statement s
     * @return SQL 语句
     */
    public abstract String getFinalSQL(Statement statement);

    /**
     * 获取分页 SQL
     *
     * @param sql    原始 SQL
     * @param offset 偏移量
     * @param limit  行数
     * @return SQL 语句
     */
    public abstract String getLimitSQL(String sql, Long offset, Long limit);

    /**
     * 创建数据源
     *
     * @param url        a
     * @param username   a
     * @param password   a
     * @param parameters a
     * @return a
     */
    public abstract DataSource createDataSource(String url, String username, String password, String[] parameters);

    /**
     * 获取建表语句
     *
     * @return s
     */
    public final String getCreateTableDDL(Table<?> tableInfo) {
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

    public final List<String> getCreateDefinition(Table<?> table) {
        var createDefinitions = new ArrayList<String>();
        createDefinitions.addAll(getColumnDefinitions(table.columns()));
        createDefinitions.addAll(getTableConstraint(table));
        return createDefinitions;
    }

    public final List<String> getColumnDefinitions(Column[] columns) {
        var list = new ArrayList<String>();
        for (var column : columns) {
            list.add(getColumnDefinition(column));
        }
        return list;
    }

    public List<String> getTableConstraint(Table<?> table) {
        return new ArrayList<>();
    }

    public final String getColumnDefinition(Column column) {
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

    public final String getDataTypeDefinition(Column column) {
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

    /**
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    public abstract List<String> getColumnConstraint(Column columns);

    /**
     * 根据 class 获取对应的 SQLType 类型 如果没有则返回 JSON
     *
     * @param javaType 需要获取的类型
     * @return a {@link String} object.
     */
    public abstract String getDataTypeDefinitionByClass(Class<?> javaType);

    /**
     * 默认值
     *
     * @return 默认类型值
     */
    public String defaultDateType() {
        return null;
    }

    /**
     * todo 暂时只支持添加新字段 需要同时支持 删除或修改
     * 获取 AlertTableDDL
     *
     * @param needAdds  a
     * @param tableInfo a
     */
    public final String getAlertTableDDL(Column[] needAdds, Table<?> tableInfo) {
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

    /**
     * 执行前
     *
     * @param preparedStatement a
     * @return a
     * @throws SQLException a
     */
    public PreparedStatement beforeExecuteQuery(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement;
    }

    public final <T> TypeHandler<T> findTypeHandler(Type type) {
        return typeHandlerSelector.findTypeHandler(type);
    }

}

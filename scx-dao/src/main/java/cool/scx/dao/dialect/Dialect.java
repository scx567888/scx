package cool.scx.dao.dialect;

import cool.scx.dao.ColumnMapping;
import cool.scx.sql.mapping.Column;
import cool.scx.sql.mapping.Table;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

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
     * 获取建表语句
     *
     * @return s
     */
    default String getCreateTableDDL(Table<?> tableInfo) {
        var columnDefinitions = getColumnDefinitions(List.of(tableInfo.columns()));
        var str = columnDefinitions.stream().map(c -> "    " + c).collect(Collectors.joining(",\n"));
        return "CREATE TABLE `" + tableInfo.name() + "`\n" +
                "(\n" +
                str +
                "\n);";
    }

    List<String> getColumnDefinitions(List<Column> columns);

    /**
     * todo
     *
     * @param nonExistentColumnNames a
     * @param tableName              a
     */
    default String getAlertTableDDL(List<Column> nonExistentColumnNames, String tableName) {
        var columnDefinitions = getColumnDefinitions(nonExistentColumnNames);
        var alertTableDDL = columnDefinitions.stream().map(columnDefinition -> "ADD " + columnDefinition).collect(Collectors.joining(", "));
        return "ALTER TABLE `" + tableName + "` " + alertTableDDL + ";";
    }


    /**
     * 根据 class 获取对应的 SQLType 类型 如果没有则返回 JSON
     *
     * @param javaType 需要获取的类型
     * @return a {@link String} object.
     */
    String getDataTypeDefinitionByClass(Class<?> javaType);

    /**
     * 获取 mysql 类型
     * 用于后续判断类型是否可以由 JDBC 进行 SQLType 到 JavaType 的直接转换
     * <p>
     * 例子 :
     * String 可以由 varchar 直接转换 true
     * Integer 可以由 int 直接转换 true
     * User 不可以由 json 直接转换 false
     *
     * @param javaType 需要判断的类型
     * @return r
     */
    SQLType getSQLType(Class<?> javaType);

    String getLimitSQL(String sql, Integer rowCount, Integer offset);

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

    String defaultDateType();

}

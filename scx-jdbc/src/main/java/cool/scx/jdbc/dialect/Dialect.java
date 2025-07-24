package cool.scx.jdbc.dialect;

import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.SchemaHelper;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.type_handler.TypeHandler;
import cool.scx.reflect.TypeInfo;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cool.scx.common.util.StringUtils.notEmpty;

/// 方言 用于针对不同数据库进行差异归一化
///
/// @author scx567888
/// @version 0.0.1
public interface Dialect {

    //*************************** jdbc 相关 **************************

    /// 是否可以处理
    boolean canHandle(String url);

    /// 是否可以处理
    boolean canHandle(DataSource dataSource);

    /// 是否可以处理
    boolean canHandle(Driver driver);

    /// 创建数据源
    DataSource createDataSource(String url, String username, String password, String[] parameters);

    /// 执行前处理
    default PreparedStatement beforeExecuteQuery(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement;
    }

    /// 　获取最终的 SQL, 一般用于 Debug
    String getFinalSQL(Statement statement);


    //************************* 类型处理相关 *************************

    /// 查找
    <T> TypeHandler<T> findTypeHandler(Class<?> type);

    <T> TypeHandler<T> findTypeHandler(TypeInfo type);

    /// 方言数据类型 转换为 标准数据类型
    ///
    /// @param dialectDataType 方言数据类型
    /// @return 标准数据类型
    JDBCType dialectDataTypeToJDBCType(String dialectDataType);

    /// 标准数据类型 转换为 方言数据类型
    ///
    /// @param jdbcType 标准数据类型
    /// @return 方言数据类型
    String jdbcTypeToDialectDataType(JDBCType jdbcType);


    //******************************* 语法区别相关 *******************************

    /// 将字段名或表名用数据库对应的转义符包装（如 MySQL 使用反引号）
    ///
    /// @param identifier 原始字段名或表名
    /// @return 加了转义符的 SQL 标识符
    default String quoteIdentifier(String identifier) {
        return "`" + identifier + "`";
    }

    /// false 表达式
    default String falseExpression() {
        return "FALSE";
    }

    /// true 表达式
    default String trueExpression() {
        return "TRUE";
    }

    /// 应用分页
    default String applyLimit(String sql, Long offset, Long limit) {
        if (limit == null) {
            return sql;
        }
        if (offset == null || offset == 0) {
            return sql + " LIMIT " + limit;
        } else {
            return sql + " LIMIT " + offset + "," + limit;
        }
    }

    /// 应用锁
    default String applySharedLock(String sql) {
        return sql + " FOR SHARE";
    }

    /// 应用锁
    default String applyExclusiveLock(String sql) {
        return sql + " FOR UPDATE";
    }


    //**************************** DLL 相关 *********************************

    /// 获取建表语句
    default String getCreateTableDDL(Table table) {
        var s = new StringBuilder();
        s.append("CREATE TABLE ");
        if (notEmpty(table.schema())) {
            s.append(table.schema()).append(".");
        }
        s.append(quoteIdentifier(table.name())).append("\n");
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
        s.append(quoteIdentifier(column.name())).append(" ");// 列名
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
        if (column.dataType() == null) {
            return defaultDataType();
        }
        var _dataType = column.dataType();
        var _name = _dataType.name();
        // 优先使用 jdbcType
        if (_dataType.jdbcType() != null) {
            _name = getDataTypeNameByJDBCType(_dataType.jdbcType());
        }
        return getDataTypeDefinitionByName(_name, _dataType.length());
    }

    /// 默认值
    /// todo 是否需要 ?
    ///
    /// @return 默认类型值
    default String defaultDataType() {
        return null;
    }

    default String getDataTypeNameByJDBCType(JDBCType dataType) {
        return dataType.name();
    }

    default String getDataTypeDefinitionByName(String dataType, Integer length) {
        if (length == null) {
            return dataType;
        }
        return dataType + "(" + length + ")";
    }

    /// 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
    default List<String> getColumnConstraint(Column column) {
        return new ArrayList<>();
    }

    /// 获取 AlertTableDDL
    ///
    /// @param needAdds    a
    /// @param needChanges a
    /// @param tableInfo   a
    default String getAlterTableDDL(Column[] needAdds, Column[] needRemoves, SchemaHelper.NeedChangeColumn[] needChanges, Table tableInfo) {
        var s = new StringBuilder();
        s.append("ALTER TABLE ");
        if (notEmpty(tableInfo.schema())) {
            s.append(tableInfo.schema()).append(".");
        }
        s.append(quoteIdentifier(tableInfo.name())).append("\n");

        List<String> clauses = new ArrayList<>();

        //处理需要添加的列
        for (var needAdd : needAdds) {
            var columnDefinition = getColumnDefinition(needAdd);
            var str = "    ADD COLUMN " + columnDefinition;
            clauses.add(str);
        }

        //处理需要删除的列
        for (var needRemove : needRemoves) {
            var str = "    DROP COLUMN " + quoteIdentifier(needRemove.name());
            clauses.add(str);
        }

        for (var needChange : needChanges) {
            if (needChange.verifyResult().needChangeDataType()) {
                var columnDefinition = getColumnDefinition(needChange.newColumn());
                var str = "    MODIFY COLUMN " + columnDefinition;
                clauses.add(str);
            }
        }

        if (clauses.isEmpty()) {
            return null;
        }

        s.append(String.join(",\n", clauses));
        s.append("\n;");
        return s.toString();
    }

}

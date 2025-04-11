package cool.scx.jdbc.dialect;

import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.type_handler.TypeHandler;
import cool.scx.jdbc.type_handler.TypeHandlerSelector;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/// 方言 用于针对不同数据库进行差异归一化
///
/// @author scx567888
/// @version 0.0.1
public abstract class Dialect {

    protected final TypeHandlerSelector typeHandlerSelector = new TypeHandlerSelector();

    /// 是否可以处理
    ///
    /// @param url 数据连接地址
    /// @return 是否可以处理
    public abstract boolean canHandle(String url);

    /// 是否可以处理
    ///
    /// @param dataSource 数据源
    /// @return 是否可以处理
    public abstract boolean canHandle(DataSource dataSource);

    /// 是否可以处理
    ///
    /// @param driver 驱动
    /// @return 是否可以处理
    public abstract boolean canHandle(Driver driver);

    /// 　获取最终的 SQL, 一般用于 Debug
    ///
    /// @param statement s
    /// @return SQL 语句
    public abstract String getFinalSQL(Statement statement);

    /// DDL 构建器
    ///
    /// @return ddlBuilder
    public abstract DDLBuilder ddlBuilder();

    /// 获取分页 SQL (默认采用最常见的 LIMIT 关键词分页)
    ///
    /// @param sql    原始 SQL
    /// @param offset 偏移量
    /// @param limit  行数
    /// @return SQL 语句
    public String getLimitSQL(String sql, Long offset, Long limit) {
        var limitClauses = limit == null ? "" : offset == null || offset == 0 ? " LIMIT " + limit : " LIMIT " + offset + "," + limit;
        return sql + limitClauses;
    }

    /// 创建数据源
    ///
    /// @param url        a
    /// @param username   a
    /// @param password   a
    /// @param parameters a
    /// @return a
    public abstract DataSource createDataSource(String url, String username, String password, String[] parameters);

    /// 执行前
    ///
    /// @param preparedStatement a
    /// @return a
    /// @throws SQLException a
    public PreparedStatement beforeExecuteQuery(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement;
    }

    public final <T> TypeHandler<T> findTypeHandler(Type type) {
        return typeHandlerSelector.findTypeHandler(type);
    }

    /// 方言数据类型 转换为 标准数据类型
    ///
    /// @param dialectDataType 方言数据类型
    /// @return 标准数据类型
    public abstract JDBCType dialectDataTypeToJDBCType(String dialectDataType);

    /// 标准数据类型 转换为 方言数据类型
    ///
    /// @param jdbcType 标准数据类型
    /// @return 方言数据类型
    public abstract String jdbcTypeToDialectDataType(JDBCType jdbcType);

    /// 将字段名或表名用数据库对应的转义符包装（如 MySQL 使用反引号）
    ///
    /// @param identifier 原始字段名或表名
    /// @return 加了转义符的 SQL 标识符
    public abstract String quoteIdentifier(String identifier);

}

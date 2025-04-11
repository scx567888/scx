package cool.scx.jdbc.mysql;

import com.mysql.cj.PreparedQuery;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.jdbc.NonRegisteringDriver;
import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.dialect.DDLBuilder;
import cool.scx.jdbc.dialect.Dialect;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/// MySQLDialect
///
/// @author scx567888
/// @version 0.0.1
public class MySQLDialect extends Dialect {

    private static final com.mysql.cj.jdbc.NonRegisteringDriver DRIVER = initDRIVER();
    private static final MySQLDDLBuilder MYSQL_DDL_BUILDER = new MySQLDDLBuilder();

    private static NonRegisteringDriver initDRIVER() {
        try {
            return new com.mysql.cj.jdbc.NonRegisteringDriver();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canHandle(String url) {
        try {
            return DRIVER.acceptsURL(url);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean canHandle(DataSource dataSource) {
        try {
            return dataSource instanceof MysqlDataSource || dataSource.isWrapperFor(MysqlDataSource.class);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean canHandle(Driver driver) {
        return driver instanceof com.mysql.cj.jdbc.Driver;
    }

    @Override
    public String getFinalSQL(Statement preparedStatement) {
        ClientPreparedStatement clientPreparedStatement;
        try {
            clientPreparedStatement = preparedStatement.unwrap(ClientPreparedStatement.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        var preparedQuery = ((PreparedQuery) clientPreparedStatement.getQuery());
        var finalSQL = preparedQuery.asSql();
        var batchedArgsSize = preparedQuery.getBatchedArgs() == null ? 0 : preparedQuery.getBatchedArgs().size();
        return batchedArgsSize > 1 ? finalSQL + "... 额外的 " + (batchedArgsSize - 1) + " 项" : finalSQL;
    }

    @Override
    public DDLBuilder ddlBuilder() {
        return MYSQL_DDL_BUILDER;
    }

    @Override
    public DataSource createDataSource(String url, String username, String password, String[] parameters) {
        var mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(url);
        mysqlDataSource.setUser(username);
        mysqlDataSource.setPassword(password);
        // 设置参数值
        for (var parameter : parameters) {
            var p = parameter.split("=");
            if (p.length == 2) {
                var property = mysqlDataSource.getProperty(p[0]);
                property.setValue(property.getPropertyDefinition().parseObject(p[1], null));
            }
        }
        return mysqlDataSource;
    }

    @Override
    public PreparedStatement beforeExecuteQuery(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setFetchSize(Integer.MIN_VALUE);
        return preparedStatement;
    }

    @Override
    public JDBCType dialectDataTypeToJDBCType(String dialectDataType) {
        return MySQLDialectHelper.dialectDataTypeToJDBCType(dialectDataType);
    }

    @Override
    public String jdbcTypeToDialectDataType(JDBCType jdbcType) {
        return MySQLDialectHelper.jdbcTypeToDialectDataType(jdbcType).getName();
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return "`" + identifier + "`";
    }

}

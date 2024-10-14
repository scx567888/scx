package cool.scx.jdbc.sql_server;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.dialect.DDLBuilder;
import cool.scx.jdbc.dialect.Dialect;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLServerDialect extends Dialect {

    private static final SQLServerDriver DRIVER = initDRIVER();

    private static SQLServerDriver initDRIVER() {
        return new SQLServerDriver();
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
            return dataSource instanceof SQLServerDataSource || dataSource.isWrapperFor(SQLServerDataSource.class);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean canHandle(Driver driver) {
        return driver instanceof SQLServerDriver;
    }

    @Override
    public String getFinalSQL(Statement statement) {
        return "";
    }

    @Override
    public DDLBuilder ddlBuilder() {
        return null;
    }

    @Override
    public DataSource createDataSource(String url, String username, String password, String[] parameters) {
        var sqlServerDataSource = new SQLServerDataSource();
        sqlServerDataSource.setURL(url);
        sqlServerDataSource.setUser(username);
        sqlServerDataSource.setPassword(password);
        return sqlServerDataSource;
    }

    @Override
    public JDBCType dialectDataTypeToJDBCType(String dialectDataType) {
        return null;
    }

    @Override
    public String jdbcTypeToDialectDataType(JDBCType jdbcType) {
        return "";
    }

}

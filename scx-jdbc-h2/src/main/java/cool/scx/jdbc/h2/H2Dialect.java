package cool.scx.jdbc.h2;

import cool.scx.jdbc.JDBCHelper;
import cool.scx.jdbc.dialect.DDLBuilder;
import cool.scx.jdbc.dialect.Dialect;
import org.h2.command.Command;
import org.h2.command.CommandInterface;
import org.h2.command.CommandRemote;
import org.h2.jdbc.JdbcPreparedStatement;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.value.Value;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

/**
 * TODO 待完成
 */
public class H2Dialect extends Dialect {

    private static final org.h2.Driver DRIVER;
    private static final Field JdbcPreparedStatement_command;
    private static final Field JdbcPreparedStatement_batchParameters;
    private static final Field CommandRemote_sql;
    private static final Field Command_sql;
    private static final H2DDLBuilder H2_DDL_BUILDER;

    static {
        try {
            //反射获取 私有字段 方便打印 SQL
            JdbcPreparedStatement_command = JdbcPreparedStatement.class.getDeclaredField("command");
            JdbcPreparedStatement_command.setAccessible(true);
            JdbcPreparedStatement_batchParameters = JdbcPreparedStatement.class.getDeclaredField("batchParameters");
            JdbcPreparedStatement_batchParameters.setAccessible(true);
            CommandRemote_sql = CommandRemote.class.getDeclaredField("sql");
            CommandRemote_sql.setAccessible(true);
            Command_sql = Command.class.getDeclaredField("sql");
            Command_sql.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        DRIVER = new org.h2.Driver();
        H2_DDL_BUILDER = new H2DDLBuilder();
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
            return dataSource instanceof JdbcDataSource || dataSource.isWrapperFor(JdbcDataSource.class);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean canHandle(Driver driver) {
        return driver instanceof org.h2.Driver;
    }

    @Override
    public String getFinalSQL(Statement preparedStatement) {
        JdbcPreparedStatement jdbcPreparedStatement;
        try {
            jdbcPreparedStatement = preparedStatement.unwrap(JdbcPreparedStatement.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            var command = (CommandInterface) JdbcPreparedStatement_command.get(jdbcPreparedStatement);
            var batchParameters = (List<?>) JdbcPreparedStatement_batchParameters.get(jdbcPreparedStatement);
            var batchedArgsSize = batchParameters == null ? 0 : batchParameters.size();
            var sql = "";
            var parameters = new Object[]{};
            if (command instanceof CommandRemote) {
                sql = (String) CommandRemote_sql.get(command);
            } else if (command instanceof Command) {
                sql = (String) Command_sql.get(command);
            }
            if (batchedArgsSize > 0) { //批量插入时 使用 第一个组参数作为 SQL 格式化的 参数
                var o = (Value[]) batchParameters.get(0);
                parameters = Arrays.stream(o).map(Value::getString).toArray();
                var finalSQL = JDBCHelper.getSqlWithValues(sql, parameters);
                return finalSQL + "... 额外的 " + (batchedArgsSize - 1) + " 项";
            } else {
                parameters = command.getParameters().stream().map(c -> c.getParamValue().getString()).toArray();
                return JDBCHelper.getSqlWithValues(sql, parameters);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public DDLBuilder ddlBuilder() {
        return H2_DDL_BUILDER;
    }

    @Override
    public DataSource createDataSource(String url, String username, String password, String[] parameters) {
        var h2DataSource = new JdbcDataSource();
        h2DataSource.setUrl(url);
        h2DataSource.setUser(username);
        h2DataSource.setPassword(password);
        return h2DataSource;
    }

}

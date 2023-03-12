package cool.scx.sql;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ServiceLoader;

public class JDBCHelperRegistry {

    private static final List<JDBCHelper> LIST;

    static {
        var load = ServiceLoader.load(JDBCHelper.class);
        LIST = load.stream().map(ServiceLoader.Provider::get).toList();
    }

    public static String getSQL(Statement statement) {
        for (var scxSpySQLHelper : LIST) {
            if (scxSpySQLHelper.canHandler(statement)) {
                return scxSpySQLHelper.getSQL(statement);
            }
        }
        return statement.toString();
    }

    public static void fixTable(TableInfo<?> userTableInfo, String databaseName, DataSource mySQLDataSource) throws SQLException {
        for (var scxSpySQLHelper : LIST) {
            if (scxSpySQLHelper.canHandler(mySQLDataSource)) {
                scxSpySQLHelper.fixTable(userTableInfo, databaseName, mySQLDataSource);
            }
        }
    }

    public static String getMySQLTypeCreateName(Class<?> type) {
        return null;
    }

}

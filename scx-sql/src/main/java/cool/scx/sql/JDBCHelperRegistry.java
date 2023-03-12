package cool.scx.sql;

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

}

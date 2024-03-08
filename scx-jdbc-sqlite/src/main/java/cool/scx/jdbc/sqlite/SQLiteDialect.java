package cool.scx.jdbc.sqlite;

import cool.scx.jdbc.JDBCHelper;
import cool.scx.jdbc.dialect.DDLBuilder;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.sqlite.type_handler.SQLiteLocalDateTimeTypeHandler;
import org.sqlite.SQLiteDataSource;
import org.sqlite.core.CorePreparedStatement;
import org.sqlite.core.CoreStatement;
import org.sqlite.jdbc4.JDBC4PreparedStatement;

import javax.sql.DataSource;
import java.lang.System.Logger;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

/**
 * @see <a href="https://www.sqlite.org/lang_createtable.html">https://www.sqlite.org/lang_createtable.html</a>
 */
public class SQLiteDialect extends Dialect {

    public static final Logger logger = System.getLogger(SQLiteDialect.class.getName());
    static final Field CoreStatement_sql;
    static final Field CoreStatement_batch;
    static final Field CorePreparedStatement_batchQueryCount;
    private static final SQLiteDDLBuilder SQLite_DDL_BUILDER;
    private static final org.sqlite.JDBC DRIVER;

    static {
        try {
            CoreStatement_sql = CoreStatement.class.getDeclaredField("sql");
            CoreStatement_batch = CoreStatement.class.getDeclaredField("batch");
            CorePreparedStatement_batchQueryCount = CorePreparedStatement.class.getDeclaredField("batchQueryCount");
            CoreStatement_sql.setAccessible(true);
            CoreStatement_batch.setAccessible(true);
            CorePreparedStatement_batchQueryCount.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            DRIVER = new org.sqlite.JDBC();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        SQLite_DDL_BUILDER = new SQLiteDDLBuilder();
    }

    public SQLiteDialect() {
        // 注册自定义的 TypeHandler       
        typeHandlerSelector.registerTypeHandler(LocalDateTime.class, new SQLiteLocalDateTimeTypeHandler());
    }

    @Override
    public boolean canHandle(String url) {
        return DRIVER.acceptsURL(url);
    }

    @Override
    public boolean canHandle(DataSource dataSource) {
        try {
            return dataSource instanceof SQLiteDataSource || dataSource.isWrapperFor(SQLiteDataSource.class);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean canHandle(Driver driver) {
        return driver instanceof org.sqlite.JDBC;
    }

    @Override
    public String getFinalSQL(Statement statement) {
        CorePreparedStatement corePreparedStatement;
        try {
            corePreparedStatement = statement.unwrap(JDBC4PreparedStatement.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        final String sql;
        final Object[] batch;
        final int batchQueryCount;
        try {
            sql = (String) CoreStatement_sql.get(corePreparedStatement);
            batch = (Object[]) CoreStatement_batch.get(corePreparedStatement);
            batchQueryCount = (int) CorePreparedStatement_batchQueryCount.get(corePreparedStatement);
        } catch (IllegalAccessException e) {
            return null;
        }
        var finalSQL = JDBCHelper.getSqlWithValues(sql, batch);
        return batchQueryCount > 1 ? finalSQL + "... 额外的 " + (batchQueryCount - 1) + " 项" : finalSQL;
    }

    @Override
    public DDLBuilder ddlBuilder() {
        return SQLite_DDL_BUILDER;
    }

    @Override
    public DataSource createDataSource(String url, String username, String password, String[] parameters) {
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl(url);
        return sqLiteDataSource;
    }

}

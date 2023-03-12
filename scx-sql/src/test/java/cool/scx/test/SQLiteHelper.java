package cool.scx.test;

import cool.scx.sql.ColumnInfo;
import cool.scx.sql.JDBCHelper;
import cool.scx.sql.JDBCHelperRegistry;
import cool.scx.sql.TableInfo;
import org.sqlite.jdbc4.JDBC4PreparedStatement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SQLiteHelper implements JDBCHelper {

    @Override
    public List<String> getTableAllColumnNames(Connection con, String databaseName, String tableName) throws SQLException {
        return null;
    }

    @Override
    public boolean checkNeedFixTable(TableInfo<?> tableInfo, String databaseName, DataSource dataSource) throws SQLException {
        return false;
    }

    @Override
    public String getCreateTableDDL(TableInfo<?> tableInfo) {
        return null;
    }

    @Override
    public String initNormalDDL(ColumnInfo column) {
        return null;
    }

    @Override
    public String[] initSpecialDDL(ColumnInfo column) {
        return new String[0];
    }

    @Override
    public void fixTable(TableInfo<?> tableInfo, String databaseName, DataSource dataSource) throws SQLException {

    }

    @Override
    public String getAlertTableDDL(List<? extends ColumnInfo> nonExistentColumnName, String tableName) {
        return null;
    }

    @Override
    public boolean canHandler(Statement preparedStatement) {
        return preparedStatement instanceof JDBC4PreparedStatement;
    }



}

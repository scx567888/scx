package cool.scx.test;

import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.dao.SchemaHelper;
import cool.scx.dao.Spy;
import cool.scx.dao.mapping.TableInfo;
import org.testng.annotations.Test;

import javax.sql.DataSource;

import java.sql.SQLException;

import static com.mysql.cj.conf.PropertyKey.*;

public class SchemaHelperTest {

    private static DataSource getMySQLDataSource() {
        var mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
//        mysqlDataSource.setDatabaseName(databaseName);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("root");
        mysqlDataSource.setPort(3306);
        // 设置参数值
        mysqlDataSource.getProperty(allowMultiQueries).setValue(true);
        mysqlDataSource.getProperty(rewriteBatchedStatements).setValue(true);
        mysqlDataSource.getProperty(createDatabaseIfNotExist).setValue(true);
        return Spy.wrap(mysqlDataSource);
    }

    public static void main(String[] args) throws SQLException {
        test1(getMySQLDataSource());
    }

    @Test
    public static void test1(DataSource dataSource) throws SQLException {
        TableInfo<?>[] edus = SchemaHelper.getTableInfoFromDataSource("edu", dataSource);
        System.out.println();
    }

}

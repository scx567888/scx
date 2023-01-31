package cool.scx.test;

import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
import cool.scx.sql.SQL;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.UpdateResult;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mysql.cj.conf.PropertyKey.*;
import static cool.scx.sql.SQL.ofNormal;

public class ScxDaoTest {

    private static final String databaseName = "scx_sql_test";
    private static final DataSource dataSource = getMySQLDataSource();
    private static final SQLRunner sqlRunner = new SQLRunner(dataSource);
    private static final String tableName = "`scx_sql_test`.`t1`";

    static {
        ScxLoggerFactory.defaultConfig().setLevel(ScxLoggingLevel.DEBUG);
    }

    public static void main(String[] args) {
        beforeTest();
        test1();
    }

    @BeforeTest
    public static void beforeTest() {
        try {
            sqlRunner.execute(ofNormal("drop table if exists " + tableName + ";" + " create table " + tableName + "(`name` varchar(32) unique ,`age` integer,`sex` boolean )"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public static void test1() {
        var sql = "insert into " + tableName + "(name, age, sex) values (:name, :age, :sex)";
        var m = new HashMap<String, Object>();
        m.put("age", 18);
        m.put("name", "小明");
        m.put("sex", 1);
        UpdateResult update = sqlRunner.update(SQL.ofNamedParameter(sql, m));
        System.out.println("具名参数插入单条数据 : " + update);
        var ms = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 999; i = i + 1) {
            var m1 = new HashMap<String, Object>();
            m1.put("age", 18 + i);
            m1.put("sex", 0);
            m1.put("name", "小明" + i);
            ms.add(m1);
        }
        UpdateResult update1 = sqlRunner.updateBatch(SQL.ofNamedParameter(sql, ms));
        System.out.println("具名参数批量插入多条数据 : " + update1);
    }


    private static MysqlDataSource getMySQLDataSource() {
        var mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setDatabaseName(databaseName);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("root");
        mysqlDataSource.setPort(3306);
        // 设置参数值
        mysqlDataSource.getProperty(allowMultiQueries).setValue(true);
        mysqlDataSource.getProperty(rewriteBatchedStatements).setValue(true);
        mysqlDataSource.getProperty(createDatabaseIfNotExist).setValue(true);
        return mysqlDataSource;
    }

}

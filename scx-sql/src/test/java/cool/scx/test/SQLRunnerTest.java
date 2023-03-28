package cool.scx.test;

import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.UpdateResult;
import cool.scx.sql.sql.SQL;
import cool.scx.test.bean.Student;
import cool.scx.test.bean.StudentRecord;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ScxExceptionHelper;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mysql.cj.conf.PropertyKey.*;
import static cool.scx.sql.result_handler.ResultHandler.ofBeanList;
import static cool.scx.sql.result_handler.ResultHandler.ofMapList;
import static cool.scx.sql.sql.SQL.ofNormal;

public class SQLRunnerTest {

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
        test2();
        test3();
        test4();
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

    @Test
    public static void test2() {
        var sql = "insert into " + tableName + "(name, age, sex) values (?, ?, ?)";
        var m = new Object[]{"小蓝", 22, 1};
        UpdateResult update = sqlRunner.update(SQL.ofPlaceholder(sql, m));
        System.out.println("占位符参数插入单条数据 : " + update);
        var ms = new ArrayList<Object[]>();
        for (int i = 0; i < 999; i = i + 1) {
            var m1 = new Object[]{"小蓝" + i, 22 + i, 0};
            ms.add(m1);
        }
        UpdateResult update1 = sqlRunner.updateBatch(SQL.ofPlaceholder(sql, ms));
        System.out.println("占位符参数批量插入多条数据 : " + update1);
    }

    @Test
    public static void test3() {
        List<Student> query = sqlRunner.query(ofNormal("select * from " + tableName), ofBeanList(Student.class));
        System.out.println("查询 使用 BeanList 总条数: " + query.size());
        System.out.println("查询 使用 BeanList 第一条内容: " + ObjectUtils.toJson(query.get(0), ""));
        List<Map<String, Object>> query1 = sqlRunner.query(ofNormal("select * from `t1`"), ofMapList());
        System.out.println("查询 使用 MapList 总条数: " + query1.size());
        System.out.println("查询 使用 MapList 第一条内容: " + ObjectUtils.toJson(query1.get(0), ""));
    }

    @Test
    public static void test4() {
        List<Student> query = sqlRunner.query(ofNormal("select * from " + tableName), ofBeanList(Student.class));
        System.out.println("当前总条数: " + query.size());
        try {
            sqlRunner.autoTransaction(() -> {

                var sql = "insert into " + tableName + "(name, age, sex) values (?, ?, ?)";
                var m = new Object[]{"小李", 22, 1};

                sqlRunner.update(SQL.ofPlaceholder(sql, m));
                List<Student> query1 = sqlRunner.query(ofNormal("select * from " + tableName), ofBeanList(Student.class));
                System.out.println("当前总条数: " + query1.size());

                sqlRunner.update(SQL.ofPlaceholder(sql, m));
            });
        } catch (Exception e) {
            ScxExceptionHelper.getRootCause(e).printStackTrace();
        }
        List<StudentRecord> query2 = sqlRunner.query(ofNormal("select * from " + tableName), ofBeanList(StudentRecord.class));
        System.out.println("回滚后总条数: " + query2.size());
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

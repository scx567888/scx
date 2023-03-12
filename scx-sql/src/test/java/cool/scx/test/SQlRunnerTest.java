package cool.scx.test;

import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.logging.ScxLogRecord;
import cool.scx.logging.ScxLogRecordFormatter;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
import cool.scx.logging.recorder.ConsoleRecorder;
import cool.scx.sql.*;
import cool.scx.sql.result_handler.BeanListHandler;
import cool.scx.sql.result_handler.MapListHandler;
import cool.scx.sql.spy.JDBCSpy;
import cool.scx.sql.spy.event.LoggingEventListener;
import cool.scx.test.bean.Student;
import cool.scx.test.bean.StudentRecord;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ScxExceptionHelper;
import org.sqlite.SQLiteDataSource;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mysql.cj.conf.PropertyKey.*;
import static cool.scx.logging.formatter.DefaultFormatter.formatStackTrace;
import static cool.scx.logging.formatter.DefaultFormatter.formatTimeStamp;
import static cool.scx.sql.SQL.ofNormal;

public class SQlRunnerTest {

    private static final String databaseName = "scx_sql_test";
    private static final DataSource dataSource = getSQLiteDataSource();
    private static final SQLRunner sqlRunner = new SQLRunner(dataSource);
    private static final String tableName = "`t1`";

    static {
        ScxLoggerFactory.defaultConfig().setLevel(ScxLoggingLevel.DEBUG);
        ScxLoggerFactory.getLogger(LoggingEventListener.class)
                .config().addRecorder(new ConsoleRecorder().setFormatter(new ScxLogRecordFormatter() {
                    @Override
                    public String format(ScxLogRecord event) {
                        // 创建初始的 message 格式如下
                        // 时间戳                    线程名称  日志级别 日志名称                       具体内容
                        // 2020-01-01 11:19:55.356 [main-1] ERROR cool.scx.xxx.TestController - 日志消息 !!!
                        var sw = new StringWriter().append(formatTimeStamp(event.timeStamp()))
                                .append(" ").append("JDBCSpy").append(" - ")
                                .append(event.message()).append(System.lineSeparator());

                        // throwable 和 stackTraceInfo 没必要同时出现
                        if (event.throwable() != null) {
                            event.throwable().printStackTrace(new PrintWriter(sw));
                        } else if (event.contextStack() != null) {
                            sw.append(formatStackTrace(event.contextStack()));
                        }
                        return sw.toString();
                    }
                }));
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
            sqlRunner.execute(ofNormal("drop table if exists " + tableName + ";"));
            sqlRunner.execute(ofNormal("create table " + tableName + "(`name` varchar(32) unique ,`age` integer,`sex` boolean )"));
            System.out.println();
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
        List<Student> query = sqlRunner.query(ofNormal("select * from " + tableName), new BeanListHandler<>(BeanBuilder.of(Student.class)));
        System.out.println("查询 使用 BeanList 总条数: " + query.size());
        System.out.println("查询 使用 BeanList 第一条内容: " + ObjectUtils.toJson(query.get(0), ""));
        List<Map<String, Object>> query1 = sqlRunner.query(ofNormal("select * from `t1`"), new MapListHandler());
        System.out.println("查询 使用 MapList 总条数: " + query1.size());
        System.out.println("查询 使用 MapList 第一条内容: " + ObjectUtils.toJson(query1.get(0), ""));
    }

    @Test
    public static void test4() {
        List<Student> query = sqlRunner.query(ofNormal("select * from " + tableName), new BeanListHandler<>(BeanBuilder.of(Student.class)));
        System.out.println("当前总条数: " + query.size());
        try {
            sqlRunner.autoTransaction(() -> {

                var sql = "insert into " + tableName + "(name, age, sex) values (?, ?, ?)";
                var m = new Object[]{"小李", 22, 1};

                sqlRunner.update(SQL.ofPlaceholder(sql, m));
                List<Student> query1 = sqlRunner.query(ofNormal("select * from " + tableName), new BeanListHandler<>(BeanBuilder.of(Student.class)));
                System.out.println("当前总条数: " + query1.size());

                sqlRunner.update(SQL.ofPlaceholder(sql, m));
            });
        } catch (Exception e) {
            ScxExceptionHelper.getRootCause(e).printStackTrace();
        }
        List<StudentRecord> query2 = sqlRunner.query(ofNormal("select * from " + tableName), new BeanListHandler<>(BeanBuilder.of(StudentRecord.class)));
        System.out.println("回滚后总条数: " + query2.size());
    }

    private static DataSource getMySQLDataSource() {
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

    private static DataSource getSQLiteDataSource() {
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl("jdbc:sqlite:aaaa.sqlite");
//        sqLiteDataSource.setDatabaseName("main");
        return JDBCSpy.wrap(sqLiteDataSource);
    }

}

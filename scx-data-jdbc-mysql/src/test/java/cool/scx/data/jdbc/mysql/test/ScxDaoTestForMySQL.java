package cool.scx.data.jdbc.mysql.test;

import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.jdbc.JDBCContext;
import cool.scx.data.jdbc.JDBCDao;
import cool.scx.data.jdbc.meta_data.SchemaHelper;
import cool.scx.data.jdbc.spy.Spy;
import cool.scx.logging.ScxLoggerFactory;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mysql.cj.conf.PropertyKey.*;
import static cool.scx.data.Query.query;
import static cool.scx.data.jdbc.sql.SQL.ofNormal;
import static cool.scx.data.query.Logic.and;
import static cool.scx.data.query.Logic.or;
import static cool.scx.data.query.WhereBody.*;
import static cool.scx.data.query.WhereOption.USE_JSON_EXTRACT;
import static java.lang.System.Logger.Level.DEBUG;

public class ScxDaoTestForMySQL {

    public static final String databaseName = "scx_dao_test";

    static {
        ScxLoggerFactory.defaultConfig().setLevel(DEBUG);
    }

    public static DataSource getMySQLDataSource() {
        var mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("root");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setDatabaseName(databaseName);
        // 设置参数值
        mysqlDataSource.getProperty(allowMultiQueries).setValue(true);
        mysqlDataSource.getProperty(rewriteBatchedStatements).setValue(true);
        mysqlDataSource.getProperty(createDatabaseIfNotExist).setValue(true);
        return Spy.wrap(mysqlDataSource);
    }

    public static void main(String[] args) throws SQLException {
        test1();
    }

    @Test
    public static void test1() throws SQLException {
        DataSource mySQLDataSource = getMySQLDataSource();
        test1_1(mySQLDataSource);
    }

    public static void test1_1(DataSource dataSource) throws SQLException {
        var jdbcContext = new JDBCContext(dataSource);
        var sqlRunner = jdbcContext.sqlRunner();
        //创建 tableInfo
        var userTableInfo = new AnnotationConfigTable(User.class);
        //删除原有的表数据
        sqlRunner.execute(ofNormal("drop table if exists " + userTableInfo.name() + ";"));
        //根据 tableInfo 生成表结构
        SchemaHelper.fixTable(userTableInfo, jdbcContext);

        //开始使用
        var userDao = new JDBCDao<>(User.class, jdbcContext);

        var list = new ArrayList<User>();

        for (int i = 0; i < 999; i = i + 1) {
            var m1 = new User();
            m1.age = i;
            m1.name = "小明" + i;
            m1.createDate = LocalDateTime.now();
            m1.tags = new String[]{"abc", String.valueOf(i)};
            var userInfo = new User.UserInfo();
            userInfo.email = i + "@test.com";
            m1.userInfo = userInfo;
            list.add(m1);
        }

        var newIds = userDao.add(list);

        System.out.println("JDBCDao-MySQL 插入 : " + newIds.size());

        //创建 query
        var query1 = query().where(gt("age", 300));
        var query2 = query().where("(age > 400 OR ", eq("name", "小明1"), ")");
        var query3 = query().where(eq("age", 10), " and ", or("age > 400", eq("name", "小明1"), and(in("name", new String[]{"小明2", "小明3"}))));
        var query4 = query().where(eq("userInfo.email", "88@test.com", USE_JSON_EXTRACT));
        var query5 = query().where(jsonContains("tags", List.of("abc")));

        //标准查询
        var a1 = userDao.find(query1);
        System.out.println("JDBCDao-MySQL 查询 1 : " + a1.size());

        //拼接查询
        var a2 = userDao.find(query2);
        System.out.println("JDBCDao-MySQL 查询 2 : " + a2.size());

        // json 查询
        var a3 = userDao.find(query3);
        System.out.println("JDBCDao-MySQL 查询 3 : " + a3.size());

        var a4 = userDao.find(query4);
        System.out.println("JDBCDao-MySQL 查询 4 : " + a4.size());

        var a5 = userDao.find(query5);
        System.out.println("JDBCDao-MySQL 查询 5 : " + a5.size());

    }

}

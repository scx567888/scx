package cool.scx.test;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.xdevapi.SessionFactory;
import cool.scx.dao.AnnotationConfigTable;
import cool.scx.dao.Query;
import cool.scx.dao.SchemaHelper;
import cool.scx.dao.impl.sql_dao.SQLDao;
import cool.scx.dao.impl.xdevapi_dao.MySQLXDao;
import cool.scx.dao.query.WhereBody;
import cool.scx.dao.query.WhereOption;
import cool.scx.dao.spy.Spy;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
import cool.scx.sql.SQLRunner;
import cool.scx.util.reflect.ClassUtils;
import org.sqlite.SQLiteDataSource;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.mysql.cj.conf.PropertyKey.*;
import static cool.scx.dao.ColumnFilter.ofExcluded;
import static cool.scx.sql.sql.SQL.ofNormal;

public class ScxDaoTest {

    public static final Path TempSQLite;
    public static final String databaseName = "scx_dao_test";
    public static Path AppRoot;

    static {
        ScxLoggerFactory.defaultConfig().setLevel(ScxLoggingLevel.DEBUG);
        try {
            AppRoot = ClassUtils.getAppRoot(ClassUtils.getCodeSource(ScxDaoTest.class));
            TempSQLite = AppRoot.resolve("temp").resolve("temp.sqlite");
            Files.createDirectories(TempSQLite.getParent());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
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

    public static DataSource getSQLiteDataSource() {
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl("jdbc:sqlite:" + TempSQLite);
        return Spy.wrap(sqLiteDataSource);
    }

    public static void main(String[] args) throws SQLException {
        test1();
    }

    @Test
    public static void test1() throws SQLException {
        DataSource mySQLDataSource = getMySQLDataSource();
        test1_1(mySQLDataSource);
        test1_1(getSQLiteDataSource());
    }

    public static void test1_1(DataSource dataSource) throws SQLException {
        SQLRunner sqlRunner = new SQLRunner(dataSource);
        //创建 tableInfo
        var userTableInfo = new AnnotationConfigTable(User.class);
        //删除原有的表数据
        sqlRunner.execute(ofNormal("drop table if exists " + userTableInfo.name() + ";"));
        sqlRunner.execute(ofNormal("drop table if exists " + userTableInfo.name() + "_doc;"));
        //根据 tableInfo 生成表结构
        SchemaHelper.fixTable(userTableInfo, databaseName, dataSource);

        var list = new ArrayList<User>();

        for (int i = 0; i < 999; i = i + 1) {
            var m1 = new User();
            m1.age = i;
            m1.name = "小明" + i;
            m1.createDate = LocalDateTime.now();
            var userInfo = new User.UserInfo();
            userInfo.email = i + "@test.com";
            m1.userInfo = userInfo;
            list.add(m1);
        }

        //创建 query
        var query1 = new Query().greaterThan("age", 300);
        var query2 = new Query().whereSQL("( age > 400 or ", WhereBody.equal("name", "小明1"), " )");
        var query3 = new Query().equal("userInfo.email", "88@test.com", WhereOption.USE_JSON_EXTRACT);

        //开始使用
        var userDao = new SQLDao<>(User.class, dataSource);

        var xFactory = new SessionFactory();
        var session1 = xFactory.getSession("mysqlx://127.0.0.1:33060/" + databaseName + "?user=root&password=root");
        var mySQLXDao = new MySQLXDao<>(User.class, session1, userTableInfo.name() + "_doc");

        var newIds = userDao.insertBatch(list, ofExcluded());
        System.out.println("插入 : " + newIds);
        var newIds3 = mySQLXDao.insertBatch(list, ofExcluded());
        System.out.println("MySQLX 插入 : " + newIds3);

        //标准查询
        var a1 = userDao.select(query1, ofExcluded());
        System.out.println("查询 1 : " + a1.size());
        var a13 = mySQLXDao.select(query1, ofExcluded());
        System.out.println("MySQLX 查询 1 : " + a13.size());

        //拼接查询
        var a2 = userDao.select(query2, ofExcluded());
        System.out.println("查询 2 : " + a2.size());
        var a23 = mySQLXDao.select(query2, ofExcluded());
        System.out.println("MySQLX 查询 2 : " + a23.size());

        // json 查询
        var a3 = userDao.select(query3, ofExcluded());
        System.out.println("查询 3 : " + a3.size());
        var a33 = userDao.select(query3, ofExcluded());
        System.out.println("MySQLX 查询 3 : " + a33.size());
    }

}

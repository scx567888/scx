package cool.scx.test;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SessionFactory;
import cool.scx.dao.*;
import cool.scx.dao.impl.MySQLDao;
import cool.scx.dao.impl.MySQLXDao;
import cool.scx.dao.impl.OldMySQLDao;
import cool.scx.dao.impl.OldMySQLTableInfo;
import cool.scx.dao.where.WhereBody;
import cool.scx.dao.where.WhereOption;
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
import static cool.scx.sql.SQL.ofNormal;

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
        var userTableInfo = new OldMySQLTableInfo(User.class);
        //删除原有的表数据
        sqlRunner.execute(ofNormal("drop table if exists " + userTableInfo.tableName() + ";"));
        sqlRunner.execute(ofNormal("drop table if exists " + userTableInfo.tableName() + "_doc;"));
        //根据 tableInfo 生成表结构
        SchemaHelper.fixTable(userTableInfo, databaseName, dataSource);
        //开始使用
        var userDao = new MySQLDao<>(userTableInfo, User.class, sqlRunner);
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
        var newIds = userDao.insertBatch(list, UpdateFilter.ofExcluded());
        System.out.println("插入 : " + newIds);
        var a1 = userDao.select(new Query().greaterThan("id", 300), SelectFilter.ofExcluded());
        System.out.println("查询 : " + a1.size());
        var a2 = userDao.select(new Query().whereSQL("( not_id > 300 or ", WhereBody.equal("age", "123"), " )"), SelectFilter.ofExcluded());
        System.out.println("查询 : " + a2.size());
        var a3 = userDao.select(new Query().equal("userInfo.email", "88@test.com", WhereOption.USE_JSON_EXTRACT), SelectFilter.ofExcluded());
        System.out.println("查询 : " + a3.size());

        //旧版 BaseDao
        var oldUserDao = new OldMySQLDao<>(userTableInfo, User.class, sqlRunner);
        var newIds2 = oldUserDao.insertBatch(list, UpdateFilter.ofExcluded());
        System.out.println("Old 插入 : " + newIds2);
        var a12 = oldUserDao.select(new Query().greaterThan("id", 300), SelectFilter.ofExcluded());
        System.out.println("Old 查询 : " + a12.size());
        var a22 = oldUserDao.select(new Query().whereSQL("( not_id > 300 or ", WhereBody.equal("age", "123"), " )"), SelectFilter.ofExcluded());
        System.out.println("Old 查询 : " + a22.size());

        SessionFactory xFactory = new SessionFactory();
        Session session1 = xFactory.getSession("mysqlx://127.0.0.1:33060/" + databaseName + "?user=root&password=root");
        var mySQLXDao = new MySQLXDao<>(userTableInfo.tableName() + "_doc", User.class, session1);
        var newIds3 = mySQLXDao.insertBatch(list, UpdateFilter.ofExcluded());
        System.out.println("MySQLX 插入 : " + newIds3);
        var a13 = mySQLXDao.select(new Query().greaterThan("age", 300), SelectFilter.ofExcluded());
        System.out.println("MySQLX 查询 : " + a13.size());
        var a23 = mySQLXDao.select(new Query().whereSQL("( age > 400 or ", WhereBody.equal("name", "小明1"), " )"), SelectFilter.ofExcluded());
        System.out.println("MySQLX 查询 : " + a23.size());
    }

}

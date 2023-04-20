package cool.scx.data.test;

import com.mysql.cj.xdevapi.SessionFactory;
import cool.scx.data.Query;
import cool.scx.data.mysql_x.MySQLXDao;
import cool.scx.data.query.WhereBody;
import cool.scx.data.query.WhereOption;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
import cool.scx.util.reflect.ClassUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.data.mysql_x.FieldFilter.ofExcluded;
import static cool.scx.data.query.Logic.and;
import static cool.scx.data.query.Logic.or;
import static cool.scx.data.query.WhereBody.equal;

public class MySQLXDaoTest {

    public static final Path TempSQLite;
    public static final String databaseName = "scx_dao_test";
    public static Path AppRoot;

    static {
        ScxLoggerFactory.defaultConfig().setLevel(ScxLoggingLevel.DEBUG);
        try {
            AppRoot = ClassUtils.getAppRoot(ClassUtils.getCodeSource(MySQLXDaoTest.class));
            TempSQLite = AppRoot.resolve("temp").resolve("temp.sqlite");
            Files.createDirectories(TempSQLite.getParent());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        test1();
    }

    @Test
    public static void test1() throws SQLException {

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

        //创建 query
        var query1 = new Query().greaterThan("age", 300);
        var query2 = new Query().whereSQL("(age > 400 OR ", equal("name", "小明1"), ")");
        var query3 = new Query().equal("age", 10).whereSQL(" and ", or("age > 400", equal("name", "小明1"), and(WhereBody.in("name", new String[]{"小明2", "小明3"}))));
        var query4 = new Query().equal("userInfo.email", "88@test.com", WhereOption.USE_JSON_EXTRACT);

        var sessionFactory = new SessionFactory();
        var session = sessionFactory.getSession("mysqlx://127.0.0.1:33060?user=root&password=root");
        var schema = session.createSchema(databaseName, true);
        var mySQLXDao = new MySQLXDao<>(User.class, schema);

        mySQLXDao._clear();

        var newIds3 = mySQLXDao.insertBatch(list, ofExcluded());
        System.out.println("MySQLX 插入 : " + newIds3);

        //标准查询
        var a13 = mySQLXDao.select(query1, ofExcluded());
        System.out.println("MySQLX 查询 1 : " + a13.size());

        //拼接查询
        var a23 = mySQLXDao.select(query2, ofExcluded());
        System.out.println("MySQLX 查询 2 : " + a23.size());

        // json 查询
        var a33 = mySQLXDao.select(query3, ofExcluded());
        System.out.println("MySQLX 查询 3 : " + a33.size());

        var a34 = mySQLXDao.select(query4, ofExcluded());
        System.out.println("MySQLX 查询 4 : " + a34.size());

        var a5 = mySQLXDao.select(new Query().jsonContains("tags", List.of("abc")), ofExcluded());
        System.out.println("MySQLX 查询 5 : " + a5.size());
    }

}

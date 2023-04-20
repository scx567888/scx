package cool.scx.data.jdbc.sqlite.test;

import cool.scx.data.Query;
import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.jdbc.JDBCContext;
import cool.scx.data.jdbc.JDBCDao;
import cool.scx.data.jdbc.meta_data.SchemaHelper;
import cool.scx.data.jdbc.spy.Spy;
import cool.scx.data.query.WhereBody;
import cool.scx.data.query.WhereOption;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
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

import static cool.scx.data.jdbc.ColumnFilter.ofExcluded;
import static cool.scx.data.jdbc.sql.SQL.ofNormal;
import static cool.scx.data.query.Logic.and;
import static cool.scx.data.query.Logic.or;
import static cool.scx.data.query.WhereBody.equal;

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
        test1_1(getSQLiteDataSource());
    }

    public static void test1_1(DataSource dataSource) throws SQLException {
        var jdbcContext = new JDBCContext(dataSource);
        var sqlRunner = jdbcContext.sqlRunner();
        //创建 tableInfo
        var userTableInfo = new AnnotationConfigTable(User.class);
        //删除原有的表数据
        sqlRunner.execute(ofNormal("drop table if exists " + userTableInfo.name() + ";"));
        sqlRunner.execute(ofNormal("drop table if exists " + userTableInfo.name() + "_doc;"));
        //根据 tableInfo 生成表结构
        SchemaHelper.fixTable(userTableInfo, databaseName, jdbcContext);

        var list = new ArrayList<User>();

        for (int i = 0; i < 999; i = i + 1) {
            var m1 = new User();
            m1.age = i;
            m1.name = "小明" + i;
            m1.createDate = LocalDateTime.now();
            var userInfo = new User.UserInfo();
            userInfo.email = i + "@test.com";
            m1.userInfo = userInfo;
            m1.tags = new String[]{"abc", String.valueOf(i)};
            list.add(m1);
        }

        //创建 query
        var query1 = new Query().greaterThan("age", 300);
        var query2 = new Query().whereSQL("(age > 400 OR ", equal("name", "小明1"), ")");
        var query3 = new Query().equal("age", 10).whereSQL(" and ", or("age > 400", equal("name", "小明1"), and(WhereBody.in("name", new String[]{"小明2", "小明3"}))));
        var query4 = new Query().equal("userInfo.email", "88@test.com", WhereOption.USE_JSON_EXTRACT);

        //开始使用
        var userDao = new JDBCDao<>(User.class, jdbcContext);

        var newIds = userDao.insertBatch(list, ofExcluded());
        System.out.println("插入 : " + newIds);

        //标准查询
        var a1 = userDao.select(query1, ofExcluded());
        System.out.println("查询 1 : " + a1.size());

        //拼接查询
        var a2 = userDao.select(query2, ofExcluded());
        System.out.println("查询 2 : " + a2.size());

        // json 查询
        var a3 = userDao.select(query3, ofExcluded());
        System.out.println("查询 3 : " + a3.size());

        var a4 = userDao.select(query4, ofExcluded());
        System.out.println("查询 4 : " + a4.size());

    }

}

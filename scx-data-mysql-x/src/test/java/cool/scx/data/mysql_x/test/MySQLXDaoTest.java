package cool.scx.data.mysql_x.test;

import com.mysql.cj.xdevapi.SessionFactory;
import cool.scx.data.mysql_x.MySQLXDao;
import cool.scx.logging.ScxLoggerFactory;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.data.QueryBuilder.*;
import static cool.scx.data.query.WhereOption.USE_JSON_EXTRACT;
import static java.lang.System.Logger.Level.DEBUG;

public class MySQLXDaoTest {

    public static final String databaseName = "scx_dao_test";

    static {
        ScxLoggerFactory.rootConfig().setLevel(DEBUG);
    }

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {

        var sessionFactory = new SessionFactory();
        var session = sessionFactory.getSession("mysqlx://127.0.0.1:33060?user=root&password=root");
        var schema = session.createSchema(databaseName, true);
        var mySQLXDao = new MySQLXDao<>(User.class, schema);

        //清空旧数据
        mySQLXDao.clear();

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

        var newIDs = mySQLXDao.add(list);

        System.out.println("MySQLX 插入 : " + newIDs.size());

        //创建 query
        var query1 = query().where(gt("age", 300));
        var query2 = query().where("(age > 400 OR ", eq("name", "小明1"), ")");
        var query3 = query().where(eq("age", 10), " and ", or("age > 400", eq("name", "小明1"), and(in("name", new String[]{"小明2", "小明3"}))));
        var query4 = query().where(eq("userInfo.email", "88@test.com", USE_JSON_EXTRACT));
        var query5 = query().where(jsonContains("tags", List.of("abc")));

        //标准查询
        var a1 = mySQLXDao.find(query1);
        System.out.println("MySQLX 查询 1 : " + a1.size());

        //拼接查询
        var a2 = mySQLXDao.find(query2);
        System.out.println("MySQLX 查询 2 : " + a2.size());

        // json 查询
        var a3 = mySQLXDao.find(query3);
        System.out.println("MySQLX 查询 3 : " + a3.size());

        var a4 = mySQLXDao.find(query4);
        System.out.println("MySQLX 查询 4 : " + a4.size());

        var a5 = mySQLXDao.find(query5);
        System.out.println("MySQLX 查询 5 : " + a5.size());

    }

}

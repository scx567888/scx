package cool.scx.test;

import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.dao.BaseDao;
import cool.scx.dao.Query;
import cool.scx.dao.SelectFilter;
import cool.scx.dao.UpdateFilter;
import cool.scx.dao.impl.AnnotationConfigTableInfo;
import cool.scx.dao.where.WhereBody;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
import cool.scx.sql.SQLHelper;
import cool.scx.sql.SQLRunner;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.mysql.cj.conf.PropertyKey.*;
import static cool.scx.sql.SQL.ofNormal;

public class ScxDaoTest {

    private static final String databaseName = "scx_dao_test";
    private static final DataSource dataSource = getMySQLDataSource();
    private static final SQLRunner sqlRunner = new SQLRunner(dataSource);

    static {
        ScxLoggerFactory.defaultConfig().setLevel(ScxLoggingLevel.DEBUG);
    }

    public static void main(String[] args) throws SQLException {
        test1();
    }

    @Test
    public static void test1() throws SQLException {
        //创建 tableInfo
        var userTableInfo = new AnnotationConfigTableInfo(User.class);
        //删除原有的表数据
        sqlRunner.execute(ofNormal("drop table if exists " + userTableInfo.tableName() + ";"));
        //根据 tableInfo 生成表结构
        SQLHelper.fixTable(userTableInfo, databaseName, getMySQLDataSource());
        //开始使用
        var userDao = new BaseDao<>(userTableInfo, User.class, sqlRunner);
        var list = new ArrayList<User>();

        for (int i = 0; i < 999; i = i + 1) {
            var m1 = new User();
            m1.age = i;
            m1.name = "小明" + i;
            m1.createDate = LocalDateTime.now();
            list.add(m1);
        }
        var newIds = userDao.insertBatch(list, UpdateFilter.ofExcluded());
        System.out.println("插入 : " + newIds);
        var a1 = userDao.select(new Query().greaterThan("id", 300), SelectFilter.ofExcluded());
        System.out.println("查询 : " + a1.size());
        var a2 = userDao.select(new Query().whereSQL("( not_id > 300 or ", WhereBody.equal("age", "123"), " )"), SelectFilter.ofExcluded());
        System.out.println("查询 : " + a2.size());
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

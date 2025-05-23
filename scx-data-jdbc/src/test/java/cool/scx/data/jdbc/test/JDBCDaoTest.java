package cool.scx.data.jdbc.test;

import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.common.util.RandomUtils;
import cool.scx.data.jdbc.JDBCRepository;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.SchemaHelper;
import cool.scx.jdbc.spy.Spy;
import cool.scx.jdbc.sql.SQL;
import cool.scx.logging.ScxLoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.mysql.cj.conf.PropertyKey.*;
import static cool.scx.data.field_policy.FieldPolicyBuilder.exclude;

public class JDBCDaoTest {

    static {
        ScxLoggerFactory.getLogger("ScxSpy").config().setLevel(System.Logger.Level.DEBUG);
    }

    public static final DataSource dataSource = getMySQLDataSource();
    public static final JDBCContext jdbcContext = new JDBCContext(dataSource);
    public static JDBCRepository<Car> carRepository;

    public static void main(String[] args) throws SQLException {
        beforeTest();
        testAll();
    }

    private static DataSource getMySQLDataSource() {
        var mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setDatabaseName("scx");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("root");
        mysqlDataSource.setPort(3306);
        // 设置参数值
        mysqlDataSource.getProperty(allowMultiQueries).setValue(true);
        mysqlDataSource.getProperty(rewriteBatchedStatements).setValue(true);
        mysqlDataSource.getProperty(createDatabaseIfNotExist).setValue(true);
        return Spy.wrap(mysqlDataSource);
    }

    @BeforeTest
    public static void beforeTest() throws SQLException {
        jdbcContext.sqlRunner().execute(SQL.sql("drop table if exists car"));
        carRepository = new JDBCRepository<>(Car.class, jdbcContext);
        SchemaHelper.fixTable(carRepository.table(), jdbcContext);
        //清空
        carRepository.clear();
    }

    @Test
    public static void testAll() throws SQLException {
        testAdd();
        testAdd2();
    }

    public static void testAdd() throws SQLException {
        var s = new ArrayList<Long>();
        //测试单条插入
        for (int i = 0; i < 10; i++) {
            var car = new Car();
            car.name = RandomUtils.randomGet("奔驰", "宝马", "奥迪");
            car.size = i;
            car.city = "city" + i;
            //测试混合插入
            Long id = carRepository.add(car, exclude("city").expression("color", """
                    CONCAT('#',
                        LPAD(HEX(FLOOR(RAND() * 256)), 2, '0'),
                        LPAD(HEX(FLOOR(RAND() * 256)), 2, '0'),
                        LPAD(HEX(FLOOR(RAND() * 256)), 2, '0')
                      )
                    """));
            s.add(id);
        }
        Assert.assertEquals(s, List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L));
        //
        //测试列表查询 应该有 color 但是没有 city
        List<Car> cars = carRepository.find();
        for (Car car : cars) {
            if (car.city != null || car.color == null) {
                Assert.fail();
            }
        }
        //测试 map 查询 这里测试的是 map 是否和 entity 的字段名相符而不是数据库中的名字
        var carMaps = carRepository.finder().listMap();
        for (var carMAP : carMaps) {
            if (carMAP.get("city") != null || carMAP.get("color") == null || carMAP.get("size") == null) {
                Assert.fail();
            }
        }
        carRepository.clear();
    }

    @Test
    public static void testAdd2() {
        //测试批量插入
        var carList = new ArrayList<Car>();
        for (int i = 0; i < 10; i++) {
            var car = new Car();
            car.name = RandomUtils.randomGet("奔驰", "宝马", "奥迪");
            car.size = i;
            carList.add(car);
        }
        List<Long> add = carRepository.add(carList, exclude("id"));
        Assert.assertEquals(add, List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L));
    }

}

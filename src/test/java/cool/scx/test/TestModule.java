package cool.scx.test;

import cool.scx.Scx;
import cool.scx.ScxContext;
import cool.scx.ScxModule;
import cool.scx.base.BaseModelService;
import cool.scx.bo.Query;
import cool.scx.dao.ScxDaoHelper;
import cool.scx.enumeration.ScxFeature;
import cool.scx.test.car.Car;
import cool.scx.test.car.CarService;
import cool.scx.util.RandomUtils;
import cool.scx.util.Timer;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class TestModule implements ScxModule {

    public static void main(String[] args) {
        runModule();
        test0();
    }

    @BeforeTest
    public static void runModule() {
        //模拟外部参数
        var args = new String[]{"--scx.port=8888"};
        Scx.builder()
                .addModule(new TestModule())
                .setMainClass(TestModule.class)
                .setArgs(args)
                .configure(ScxFeature.SHOW_BANNER, true)
                .configure(ScxFeature.SHOW_EASY_CONFIG_INFO, true)
                .configure(ScxFeature.USE_DEVELOPMENT_ERROR_PAGE, true)
                .build().run();
        //修复表
        ScxDaoHelper.fixTable();
    }

    @Test
    public static void test0() {
        var carService = ScxContext.beanFactory().getBean(CarService.class);
        var carService1 = new BaseModelService<>(Car.class);
        try {
            if (carService1.count() < 1500) {
                System.err.println("开始: 方式1 (批量) 插入");
                //插入数据 方式1
                Timer.start("save1");
                var l = new ArrayList<Car>();
                for (int i = 0; i < 999; i++) {
                    var c = new Car();
                    c.name = RandomUtils.getRandomString(10, false) + "🤣";
                    l.add(c);
                }
                carService.save(l);
                System.err.println("完成: 方式1 (批量) 插入 999条数据时间 :" + Timer.stopToMillis("save1"));

                System.err.println("开始: 方式2 (循环单次) 插入");
                //插入数据 方式2
                Timer.start("save2");
                for (int i = 0; i < 999; i++) {
                    var c = new Car();
                    c.name = RandomUtils.getRandomString(10, false) + "😢";
                    carService1.save(c);
                }
                System.err.println("方式2 (循环单次) 插入 999条数据时间 :" + Timer.stopToMillis("save2"));
            }

            System.err.println("将 id 大于 200 的 name 设置为空 !!!");
            var c = new Car();
            c.tombstone = false;
            c.name = null;
            carService.updateIncludeNull(c, new Query().greaterThan("id", 200));

            System.err.println("查询所有数据条数 !!! : " + carService.list().size());
            System.err.println("查询所有 id 大于 200 条数 !!! : " + carService.list(new Query().greaterThan("id", 200)).size());
            System.err.println("查询所有 name 为空 条数 !!! : " + carService.list(new Query().isNull("name")).size());

            System.err.println("------------------------- 测试事务 --------------------------------");
            // 测试事务
            //插入数据 方式2
            System.err.println("事务开始前数据库中 数据条数 : " + carService.count());

            ScxContext.sqlRunner().autoTransaction(con -> {
                System.err.println("现在插入 1 数据条数");
                var bb = new Car();
                bb.name = "唯一ID";
                carService.save(con, bb);
                System.err.println("现在数据库中数据条数 : " + carService.count(con));
                System.err.println("现在在插入 1 错误数据");
                carService.save(con, bb);
            });
        } catch (Exception e) {
            System.err.println("出错了 后滚后数据库中数据条数 : " + carService.count());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        System.out.println("CarModule-Start");
    }

}

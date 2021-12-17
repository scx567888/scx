package cool.scx.test;

import cool.scx.Scx;
import cool.scx.ScxContext;
import cool.scx.ScxModule;
import cool.scx.base.BaseModelService;
import cool.scx.bo.Query;
import cool.scx.bo.UpdateFilter;
import cool.scx.dao.ScxDaoHelper;
import cool.scx.enumeration.ScxFeature;
import cool.scx.test.car.Car;
import cool.scx.test.car.CarService;
import cool.scx.util.RandomUtils;
import cool.scx.util.Timer;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronTrigger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
                .configure(ScxFeature.ENABLE_SCHEDULING_WITH_ANNOTATION, true)
                .build().run();
        //修复表
        ScxDaoHelper.fixTable();
    }

    @Test
    public static void test0() {
        var carService = ScxContext.getBean(CarService.class);
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
            carService.update(c, new Query().greaterThan("id", 200), UpdateFilter.ofExcluded(false));

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
        var logger = LoggerFactory.getLogger(TestModule.class);
        //测试定时任务
        ScxContext.scheduler().scheduleAtFixedRate((a) -> {
            logger.error("这是通过 ScxContext.scheduleAtFixedRate() 打印的 : 一共 10 次 , 这时第 " + a.runCount() + " 次执行 !!!");
        }, Instant.now().plusSeconds(3), Duration.of(1, ChronoUnit.SECONDS), 10);

        ScxContext.scheduler().schedule((a) -> {
            logger.error("这是通过 ScxContext.scheduler() 使用 Cron 表达式 打印的 : 这时第 " + a.runCount() + " 次执行 !!!");
        }, new CronTrigger("*/1 * * * * ?"));

        ScxContext.scheduler().scheduleAtFixedRate((a) -> {
            logger.error("这是通过 ScxContext.scheduleAtFixedRate() 打印的 : 不限次数 不过到 第 10 次手动取消 , 这是第 " + a.runCount() + " 次执行 !!!");
            if (a.runCount() >= 10) {
                a.scheduledFuture().cancel(false);
            }
        }, Instant.now().plusSeconds(3), Duration.of(1, ChronoUnit.SECONDS));

        System.out.println("CarModule-Start");
    }

}

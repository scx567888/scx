package cool.scx.test;

import cool.scx.Scx;
import cool.scx.ScxContext;
import cool.scx.bo.Query;
import cool.scx.dao.ScxDaoHelper;
import cool.scx.enumeration.ScxFeature;
import cool.scx.test.car_module.Car;
import cool.scx.test.car_module.CarModule;
import cool.scx.test.car_module.CarService;
import cool.scx.util.RandomUtils;
import cool.scx.util.Timer;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class CarModuleTest {

    public static void main(String[] args) {
        runModule();
    }

    @BeforeTest
    public static void runModule() {
        var args = new String[0];
        Scx scx = Scx.builder()
                .addModule(new CarModule())
                .setMainClass(CarModule.class)
                .setArgs(args)
                .configure(ScxFeature.SHOW_BANNER, true)
                .configure(ScxFeature.SHOW_EASY_CONFIG_INFO, true)
                .configure(ScxFeature.USE_DEVELOPMENT_ERROR_PAGE, true)
                .build();
        scx.run();
        //修复表 (这里忽略提示 即直接修复不询问用户)
        ScxDaoHelper.fixTable();
    }

    @Test
    public static void test0() {
        var carService = ScxContext.beanFactory().getBean(CarService.class);
        try {
            //插入数据 方式1
            Timer.start("save1");
            var l = new ArrayList<Car>();
            for (int i = 0; i < 9999; i++) {
                var c = new Car();
                c.name = RandomUtils.getRandomString(10, false);
                l.add(c);
            }
            carService.save(l);
            System.out.println("方式1 (批量) 插入 9999条数据时间 :" + Timer.stopToMillis("save1"));


            //插入数据 方式2
            Timer.start("save2");
            for (int i = 0; i < 9999; i++) {
                var c = new Car();
                c.name = RandomUtils.getRandomString(10, false);
                carService.save(c);
            }
            System.out.println("方式2 (循环) 插入 9999条数据时间 :" + Timer.stopToMillis("save2"));


            var c = new Car();
            c.tombstone = false;
            carService.updateIncludeNull(c, new Query().greaterThan("id", 2000));


            var list = carService.list();
            var list2 = carService.list(new Query().greaterThan("id", 2000));
            var list3 = carService.list(new Query().isNull("name"));


            System.out.println(list.size());
            System.out.println(list2.size());
            System.out.println(list3.size());

            // 测试事务
            //插入数据 方式2
            Timer.start("save3");
            System.out.println("当前条数 事务回滚前" + carService.count());
            ScxContext.sqlRunner().autoTransaction(con -> {
                for (int i = 0; i < 99; i++) {
                    var b = new Car();
                    b.name = RandomUtils.getRandomString(10, false);
                    Car save = carService.save(con, b);
                    System.out.println(save.name);
                }
                System.out.println("当前条数 事务马上失败前" + carService.count(con));
                var bb = new Car();
                bb.name = "唯一ID";
                //这里会抛出异常 所以事务应该回滚
                carService.save(con, List.of(bb, bb));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("当前条数 事务回滚后" + carService.count());
    }

}

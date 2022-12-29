package cool.scx.test;

import cool.scx.core.Scx;
import cool.scx.core.ScxContext;
import cool.scx.core.ScxModule;
import cool.scx.core.base.BaseModelService;
import cool.scx.core.dao.ScxDaoHelper;
import cool.scx.core.enumeration.ScxCoreFeature;
import cool.scx.sql.base.Query;
import cool.scx.sql.base.SelectFilter;
import cool.scx.sql.base.UpdateFilter;
import cool.scx.sql.where.WhereOption;
import cool.scx.test.car.Car;
import cool.scx.test.car.CarColor;
import cool.scx.test.car.CarOwner;
import cool.scx.test.car.CarService;
import cool.scx.test.person.Person;
import cool.scx.test.person.PersonService;
import cool.scx.util.*;
import cool.scx.util.http.FormData;
import cool.scx.util.http.HttpClientHelper;
import cool.scx.util.zip.UnZipBuilder;
import cool.scx.util.zip.ZipBuilder;
import cool.scx.util.zip.ZipOption;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.web.handler.FileSystemAccess;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronTrigger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.core.eventbus.ZeroCopyMessageCodec.ZERO_COPY_CODEC_NAME;
import static org.testng.Assert.assertEquals;

public class TestModule extends ScxModule {

    public static void main(String[] args) {
        runModule();
        test0();
        test1();
        test2();
        test3();
        test4();
    }

    @BeforeTest
    public static void runModule() {
        //模拟外部参数
        var args = new String[]{"--scx.port=8888"};
        Scx.builder()
                .setMainClass(TestModule.class)
                .addModule(new TestModule())
                .setArgs(args)
                .configure(ScxCoreFeature.SHOW_BANNER, true)
                .configure(ScxCoreFeature.SHOW_CORE_CONFIG_INFO, true)
                .configure(ScxCoreFeature.SHOW_MODULE_LIFE_CYCLE_INFO, true)
                .configure(ScxCoreFeature.SHOW_START_UP_INFO, true)
                .configure(ScxCoreFeature.USE_DEVELOPMENT_ERROR_PAGE, true)
                .configure(ScxCoreFeature.ENABLE_SCHEDULING_WITH_ANNOTATION, true)
                .run();
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
                StopWatch.start("save1");
                var l = new ArrayList<Car>();
                for (int i = 0; i < 999; i = i + 1) {
                    var c = new Car();
                    c.name = RandomUtils.randomString(10, false) + "🤣";
                    c.color = CarColor.values()[RandomUtils.randomNumber(0, 3)];
                    c.owner = new CarOwner("Jack", i, new String[]{"123456789", "666666666"});
                    c.tags = new String[]{"fast", "beautiful", "small", "big"};
                    l.add(c);
                }
                carService.add(l);
                System.err.println("完成: 方式1 (批量) 插入 999条数据时间 :" + StopWatch.stopToMillis("save1"));

                System.err.println("开始: 方式2 (循环单次) 插入");
                //插入数据 方式2
                StopWatch.start("save2");
                for (int i = 0; i < 999; i = i + 1) {
                    var c = new Car();
                    c.name = RandomUtils.randomString(10, false) + "😢";
                    c.color = CarColor.values()[RandomUtils.randomNumber(0, 3)];
                    c.owner = new CarOwner("David", i, new String[]{"987654321"});
                    carService1.add(c);
                }
                System.err.println("方式2 (循环单次) 插入 999条数据时间 :" + StopWatch.stopToMillis("save2"));
            }

            System.err.println("将 id 大于 200 的 name 设置为空 !!!");
            var c = new Car();
            c.name = null;
            carService.update(c, new Query().greaterThan("id", 200), UpdateFilter.ofIncluded(false).addIncluded("name"));

            System.err.println("查询所有数据条数 !!! : " + carService.list().size());
            System.err.println("查询所有 id 大于 200 条数 !!! : " + carService.list(new Query().greaterThan("id", 200)).size());
            System.err.println("查询所有 name 为空 条数 !!! : " + carService.list(new Query().isNull("name")).size());
            System.err.println("查询所有 车主为 Jack 的条数 !!! : " + carService.list(new Query().equal("owner.name", "Jack", WhereOption.USE_JSON_EXTRACT)).size());
            System.err.println("查询所有 车主年龄大于 18 的条数 !!! : " + carService.list(new Query().greaterThan("owner.age", 18, WhereOption.USE_JSON_EXTRACT)).size());
            System.err.println("查询所有 拥有 fast 和 big 标签的条数 !!! : " + carService.list(new Query().jsonContains("tags", "fast,big")).size());
            System.err.println("查询所有 汽车 中 车主 的 电话号 中 包含 666666666 的条数 !!! : " + carService.list(new Query().jsonContains("owner.phoneNumber", "666666666")).size());

            System.err.println("------------------------- 测试事务 --------------------------------");
            // 测试事务
            //插入数据 方式2
            System.err.println("事务开始前数据库中 数据条数 : " + carService.count());

            ScxContext.autoTransaction(() -> {
                System.err.println("现在插入 1 数据条数");
                var bb = new Car();
                bb.name = "唯一ID";
                bb.color = CarColor.values()[RandomUtils.randomNumber(0, 3)];
                carService.add(bb);
                System.err.println("现在数据库中数据条数 : " + carService.count());
                System.err.println("现在在插入 1 错误数据");
                carService.add(bb);
            });
        } catch (Exception e) {
            System.err.println("出错了 后滚后数据库中数据条数 : " + carService.count());
        }

    }

    @Test
    public static void test1() {
        ScxExceptionHelper.wrap(() -> FileUtils.write(ScxContext.getTempPath("test.txt"), "内容2内容2内容2内容2😂😂😂!!!".getBytes(StandardCharsets.UTF_8)));
        var ip = NetUtils.getLocalIPAddress().v4()[0];
        var logger = LoggerFactory.getLogger(TestModule.class);
        //测试 URIBuilder
        for (int i = 0; i < 1000; i = i + 1) {
            var s = "http://" + ip + ":8888/test0";
            var stringHttpResponse = HttpClientHelper.post(
                    URIBuilder.of(s)
                            .addParam("name", "小明😊123?!@%^&**()_特-殊 字=符")
                            .addParam("age", 18).toString(),
                    new FormData()
                            .addFile("content", "内容内容内容内容内容".getBytes(StandardCharsets.UTF_8), "", "")
                            .addFile("content1", ScxContext.getTempPath("test.txt"))
            ).body();
            logger.error("测试请求[{}] : {}", i, stringHttpResponse);
        }
    }

    @Test
    public static void test2() {
        var car = new Car();
        ScxContext.eventBus().consumer("test-event-bus", (c) -> {
            c.reply(car, new DeliveryOptions().setCodecName(ZERO_COPY_CODEC_NAME));
            assertEquals(c.body(), car);
        });
        ScxContext.eventBus().request("test-event-bus", car, new DeliveryOptions().setCodecName(ZERO_COPY_CODEC_NAME), c -> {
            assertEquals(c.result().body(), car);
        });
        ScxContext.eventBus().send("test-event-bus", car, new DeliveryOptions().setCodecName(ZERO_COPY_CODEC_NAME));
    }

    @Test
    public static void test3() {
        var personService = ScxContext.getBean(PersonService.class);
        var carService = ScxContext.getBean(CarService.class);
        if (personService.count() < 200) {
            List<Car> list = carService.list();
            var ps = new ArrayList<Person>();
            for (int i = 0; i < list.size(); i++) {
                var p = new Person();
                p.carID = list.get(i).id;
                p.age = i;
                ps.add(p);
            }
            personService.add(ps);
        }
        //根据所有 person 表中年龄小于 100 的 carID 查询 car 表中的数据
        var cars = carService.list(new Query().in("id",
                personService.buildListSQL(new Query().lessThan("age", 100), SelectFilter.ofIncluded("carID"))
        ));
        var logger = LoggerFactory.getLogger(TestModule.class);
        logger.error("根据所有 person 表中年龄小于 100 的 carID 查询 car 表中的数据 总条数 {}", cars.size());
        //根据所有 person 表中年龄小于 100 的 carID 查询 car 表中的数据
        var cars1 = carService.list(new Query().whereSQL("id IN ",
                personService.buildListSQL(new Query().lessThan("age", 100), SelectFilter.ofIncluded("carID"))
        ));
        logger.error("第二种方式 (whereSQL) : 根据所有 person 表中年龄小于 100 的 carID 查询 car 表中的数据 总条数 {}", cars1.size());
    }

    @Test
    public static void test4() {

        try {
            //创建一个压缩文件先
            var zipBuilder = new ZipBuilder();
            zipBuilder.put("第一个目录/第二个目录/第二个目录中的文件.txt", "文件内容".getBytes(StandardCharsets.UTF_8));
            zipBuilder.put("第一个目录/这是一系列空目录/这是一系列空目录/这是一系列空目录/这是一系列空目录/这是一系列空目录");
            zipBuilder.put("第一个目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录");
            zipBuilder.put("第一个目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/一个文本文件.txt", "一些内容,一些内容,一些内容,一些内容 下😊😂🤣❤😍😒👌😘".getBytes(StandardCharsets.UTF_8));
            zipBuilder.toFile(ScxContext.getTempPath("aaaaa.zip"));

            //解压再压缩
            new UnZipBuilder(ScxContext.getTempPath("aaaaa.zip")).toFile(ScxContext.getTempPath("hhhh"));
            new ZipBuilder(ScxContext.getTempPath("hhhh")).toFile(ScxContext.getTempPath("bbbbb.zip"));
            //重复一次
            new UnZipBuilder(ScxContext.getTempPath("bbbbb.zip")).toFile(ScxContext.getTempPath("gggggg"), ZipOption.INCLUDE_ROOT);
            new ZipBuilder(ScxContext.getTempPath("gggggg"),ZipOption.INCLUDE_ROOT).toFile(ScxContext.getTempPath("ccccc.zip"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        ScxContext.router().vertxRouter().route("/static/*")
                .handler(StaticHandler.create(FileSystemAccess.ROOT, ScxContext.getPathByAppRoot("AppRoot:c\\static").toString())
                        .setFilesReadOnly(false));
        var logger = LoggerFactory.getLogger(TestModule.class);
        //测试定时任务
        ScxContext.scheduler().scheduleAtFixedRate((a) -> {
            //测试
            logger.error("这是通过 ScxContext.scheduleAtFixedRate() 打印的 : 一共 10 次 , 这时第 " + a.runCount() + " 次执行 !!!");
        }, Instant.now().plusSeconds(3), Duration.of(1, ChronoUnit.SECONDS), 10);

        ScxContext.scheduler().schedule((a) -> {
            //测试
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

package cool.scx.core.test;

import cool.scx.common.util.*;
import cool.scx.common.zip.UnZipBuilder;
import cool.scx.common.zip.ZipBuilder;
import cool.scx.common.zip.ZipOptions;
import cool.scx.core.Scx;
import cool.scx.core.ScxContext;
import cool.scx.core.ScxModule;
import cool.scx.core.base.BaseModelService;
import cool.scx.core.enumeration.ScxCoreFeature;
import cool.scx.core.test.car.Car;
import cool.scx.core.test.car.CarColor;
import cool.scx.core.test.car.CarOwner;
import cool.scx.core.test.car.CarService;
import cool.scx.core.test.person.Person;
import cool.scx.core.test.person.PersonService;
import cool.scx.data.query.QueryOption;
import cool.scx.http.routing.handler.StaticHandler;
import cool.scx.jdbc.sql.SQL;
import cool.scx.scheduling.ScxScheduling;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cool.scx.data.field_filter.FieldFilterBuilder.ofIncluded;
import static cool.scx.data.query.QueryBuilder.*;
import static java.lang.System.Logger.Level.ERROR;

public class TestModule extends ScxModule {

    public static void main(String[] args) throws SocketException {
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
        var args = new String[]{"--scx.port=8888", "--scx.config.path=AppRoot:scx-config.json"};
        Scx.builder()
                .setMainClass(TestModule.class)
                .addModule(new TestModule())
                .setArgs(args)
                .configure(ScxCoreFeature.SHOW_BANNER, true)
                .configure(ScxCoreFeature.SHOW_OPTIONS_INFO, true)
                .configure(ScxCoreFeature.SHOW_MODULE_LIFE_CYCLE_INFO, true)
                .configure(ScxCoreFeature.SHOW_START_UP_INFO, true)
                .configure(ScxCoreFeature.USE_DEVELOPMENT_ERROR_PAGE, true)
                .configure(ScxCoreFeature.ENABLE_SCHEDULING_WITH_ANNOTATION, true)
                .configure(ScxCoreFeature.USE_SPY, true)
                .run();
        //修复表
        try {
            ScxContext.sqlRunner().execute(SQL.sql("drop database if exists scx_test; create database scx_test; use scx_test"));
        } catch (Exception ignored) {

        }
        ScxContext.scx().fixTable();
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
                for (int i = 0; i < 99; i = i + 1) {
                    var c = new Car();
                    c.name = RandomUtils.randomString(10) + "🤣";
                    c.color = CarColor.values()[RandomUtils.randomInt(4)];
                    c.owner = new CarOwner("Jack", i, new String[]{"123456789", "666666666"});
                    c.tags = new String[]{"fast", "beautiful", "small", "big"};
                    l.add(c);
                }
                carService.add(l);
                System.err.println("完成: 方式1 (批量) 插入 99条数据时间 :" + StopWatch.stopToMillis("save1"));

                System.err.println("开始: 方式2 (循环单次) 插入");
                //插入数据 方式2
                StopWatch.start("save2");
                for (int i = 0; i < 99; i = i + 1) {
                    var c = new Car();
                    c.name = RandomUtils.randomString(10) + "😢";
                    c.color = CarColor.values()[RandomUtils.randomInt(4)];
                    c.owner = new CarOwner("David", i, new String[]{"987654321"});
                    carService1.add(c);
                }
                System.err.println("方式2 (循环单次) 插入 99条数据时间 :" + StopWatch.stopToMillis("save2"));
            }

            System.err.println("将 id 大于 200 的 name 设置为空 !!!");
            var c = new Car();
            c.name = null;
            carService.update(c, query().where(gt("id", 200)), ofIncluded("name").ignoreNullValue(false));

            System.err.println("查询所有数据条数 !!! : " + carService.find().size());
            System.err.println("查询所有 id 大于 200 条数 !!! : " + carService.find(gt("id", 200)).size());
            System.err.println("查询所有 name 为空 条数 !!! : " + carService.find(isNull("name")).size());
            System.err.println("查询所有 车主为 Jack 的条数 !!! : " + carService.find(eq("owner.name", "Jack", QueryOption.USE_JSON_EXTRACT)).size());
            System.err.println("查询所有 车主年龄大于 18 的条数 !!! : " + carService.find(gt("owner.age", 18, QueryOption.USE_JSON_EXTRACT)).size());
            System.err.println("查询所有 拥有 fast 和 big 标签的条数 !!! : " + carService.find(jsonContains("tags", "fast,big")).size());
            System.err.println("查询所有 汽车 中 车主 的 电话号 中 包含 666666666 的条数 !!! : " + carService.find(jsonContains("owner.phoneNumber", "666666666")).size());

            System.err.println("------------------------- 测试事务 --------------------------------");
            // 测试事务
            //插入数据 方式2
            System.err.println("事务开始前数据库中 数据条数 : " + carService.count());

            ScxContext.autoTransaction(() -> {
                System.err.println("现在插入 1 数据条数");
                var bb = new Car();
                bb.name = "唯一ID";
                bb.color = CarColor.values()[RandomUtils.randomInt(4)];
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
    public static void test1() throws SocketException {
        ScxExceptionHelper.wrap(() -> FileUtils.write(ScxContext.getTempPath("test.txt"), "内容2内容2内容2内容2😂😂😂!!!".getBytes(StandardCharsets.UTF_8)));
        var ip = Arrays.stream(NetUtils.getLocalIPAddress()).filter(i -> i instanceof Inet4Address).toList().getFirst();
        var logger = System.getLogger(TestModule.class.getName());
        //测试 URIBuilder
        for (int i = 0; i < 1000; i = i + 1) {
            var s = "http://" + ip.getHostAddress() + ":8888/test0";
//            try {
//                var stringHttpResponse = ScxHttpClientHelper.post(
//                        URIBuilder.of(s)
//                                .addParam("name", "小明😊123?!@%^&**()_特-殊 字=符")
//                                .addParam("age", 18).toString(),
//                        new FormData()
//                                .fileUpload("content", "内容内容内容内容内容".getBytes(StandardCharsets.UTF_8), "", "")
//                                .fileUpload("content1", ScxContext.getTempPath("test.txt"))
//                ).body();
//                logger.log(ERROR, "测试请求[{0}] : {1}", i, stringHttpResponse);
//            } catch (IOException | InterruptedException ignored) {
//
//            }

        }
    }

    @Test
    public static void test2() {
        var car = new Car();
//        ScxContext.eventBus().consumer("test-event-bus", (c) -> {
//            c.reply(car, new DeliveryOptions().setCodecName(ZERO_COPY_CODEC_NAME));
//            assertEquals(c.body(), car);
//        });
//        ScxContext.eventBus().request("test-event-bus", car, new DeliveryOptions().setCodecName(ZERO_COPY_CODEC_NAME), c -> {
//            assertEquals(c.result().body(), car);
//        });
//        //通过指定 ZERO_COPY_CODEC_NAME 实现 0 拷贝
//        ScxContext.eventBus().send("test-event-bus", car, new DeliveryOptions().setCodecName(ZERO_COPY_CODEC_NAME));
//        //通过 @ZeroCopyMessage 注解实现 零拷贝
//        ScxContext.eventBus().publish("test-event-bus", car);
//        //通过 zeroCopyMessage() 包装器实现 零拷贝 (会自动脱壳)
//        ScxContext.eventBus().send("test-event-bus", zeroCopyMessage(car));
    }

    @Test
    public static void test3() {
        var personService = ScxContext.getBean(PersonService.class);
        var carService = ScxContext.getBean(CarService.class);
        if (personService.count() < 200) {
            List<Car> list = carService.find();
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
        var cars = carService.find(query().where(in("id",
                personService.buildListSQL(query().where(lt("age", 100)), ofIncluded("carID"))
        )));
        var logger = System.getLogger(TestModule.class.getName());
        logger.log(ERROR, "根据所有 person 表中年龄小于 100 的 carID 查询 car 表中的数据 总条数 {0}", cars.size());
        //根据所有 person 表中年龄小于 100 的 carID 查询 car 表中的数据
        var cars1 = carService.find(query().where("id IN ",
                personService.buildListSQL(query().where(lt("age", 100)), ofIncluded("carID"))
        ));
        logger.log(ERROR, "第二种方式 (whereSQL) : 根据所有 person 表中年龄小于 100 的 carID 查询 car 表中的数据 总条数 {0}", cars1.size());
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
            new UnZipBuilder(ScxContext.getTempPath("bbbbb.zip")).toFile(ScxContext.getTempPath("gggggg"), new ZipOptions().setIncludeRoot(true));
            new ZipBuilder(ScxContext.getTempPath("gggggg"), new ZipOptions().setIncludeRoot(true)).toFile(ScxContext.getTempPath("ccccc.zip"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Scx scx) {
        scx.scxHttpRouter().route()
                .path("/static/*")
                .handler(new StaticHandler(scx.scxEnvironment().getPathByAppRoot("AppRoot:c\\static")));
        var logger = System.getLogger(TestModule.class.getName());
        //测试定时任务
        ScxScheduling.fixedRate()
                .startTime(Instant.now().plusSeconds(3))
                .delay(Duration.of(1, ChronoUnit.SECONDS))
                .maxRunCount(10)
                .start((a) -> {
                    //测试
                    logger.log(ERROR, "这是通过 ScxContext.scheduleAtFixedRate() 打印的 : 一共 10 次 , 这时第 " + a.runCount() + " 次执行 !!!");
                });

        ScxScheduling.cron()
                .expression("*/1 * * * * ?")
                .start((a) -> {
                    //测试
                    logger.log(ERROR, "这是通过 ScxContext.scheduler() 使用 Cron 表达式 打印的 : 这时第 " + a.runCount() + " 次执行 !!!");
                });

        ScxScheduling.fixedRate()
                .startTime(Instant.now().plusSeconds(3))
                .delay(Duration.of(1, ChronoUnit.SECONDS))
                .start((a) -> {
                    logger.log(ERROR, "这是通过 ScxContext.scheduleAtFixedRate() 打印的 : 不限次数 不过到 第 10 次手动取消 , 这是第 " + a.runCount() + " 次执行 !!!");
                    if (a.runCount() >= 10) {
                        a.cancel();
                    }
                });

        System.out.println("CarModule-Start");
    }

}

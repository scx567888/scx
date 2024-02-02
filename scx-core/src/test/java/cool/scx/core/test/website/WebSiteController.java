package cool.scx.core.test.website;

import cool.scx.core.ScxContext;
import cool.scx.core.test.car.CarService;
import cool.scx.core.test.person.Person;
import cool.scx.core.test.person.PersonService;
import cool.scx.http_client.ScxHttpClientHelper;
import cool.scx.mvc.ScxMvc;
import cool.scx.mvc.annotation.FromQuery;
import cool.scx.mvc.annotation.FromUpload;
import cool.scx.mvc.annotation.ScxRoute;
import cool.scx.mvc.type.UploadedEntity;
import cool.scx.mvc.vo.*;
import cool.scx.standard.FileFormat;
import cool.scx.standard.HttpMethod;
import cool.scx.util.HashUtils;
import cool.scx.util.NetUtils;
import cool.scx.util.RandomUtils;
import cool.scx.util.ScxExceptionHelper;
import cool.scx.util.zip.ZipBuilder;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import static cool.scx.standard.ScxDateTimeFormatter.yyyy_MM_dd_HH_mm_ss;

/**
 * 简单测试
 *
 * @author scx567888
 * @version 0.3.6
 * @since 1.3.14
 */
@ScxRoute
public class WebSiteController {

    final CarService carService;

    @Autowired
    CarService carService1;

    public WebSiteController(CarService carService) {
        this.carService = carService;
    }

    @ScxRoute(methods = HttpMethod.POST)
    public static Object test0(@FromQuery String name,
                               @FromQuery Integer age,
                               @FromUpload UploadedEntity content,
                               @FromUpload FileUpload content1) {
        System.err.println("客户端 IP :" + NetUtils.getClientIPAddress(ScxMvc.routingContext().request()));
        return Map.of("now", yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()),
                "name", name, "age", age, "content", content.buffer().toString(StandardCharsets.UTF_8),
                "content1", ScxContext.vertx().fileSystem().readFileBlocking(content1.uploadedFileName()).toString(StandardCharsets.UTF_8));
    }

    @ScxRoute(methods = HttpMethod.GET)
    public static void TestTransaction(RoutingContext ctx) throws Exception {
        var personService = ScxContext.getBean(PersonService.class);
        var p1 = personService.add(new Person().setMoney(100));
        var p2 = personService.add(new Person().setMoney(200));
        var sb = new StringBuilder();
        sb.append("转账开始前: ").append("p1(").append(p1.money).append(") p2(").append(p2.money).append(")</br>");
        try {
            //模拟 转账
            ScxContext.autoTransaction(() -> {

                // todo 此处需要支持并发处理 及 同时执行 给 p1 扣钱 和 给 p2 加钱                 
                //给 p1 扣钱
                p1.money = p1.money - 50;
                var p11 = personService.update(p1);

                //给 p2 加钱
                p2.money = p2.money + 50;
                var p21 = personService.update(p2);

                sb.append("转账中: ").append("p1(")
                        .append(p11.money)
                        .append(") p2(")
                        .append(p21.money).append(")</br>");

                throw new RuntimeException("模拟发生异常 !!!");
            });
        } catch (Exception e) {
            var p11 = personService.get(p1.id);
            var p21 = personService.get(p2.id);
            sb.append("出错了 回滚后: ").append("p1(").append(p11.money).append(") p2(").append(p21.money).append(")</br>");
        }
        Html.ofString(sb.toString()).accept(ctx, ScxContext.scxMvc().templateHandler());
    }

    /**
     * 测试!!!
     *
     * @return 页面
     * @throws java.io.IOException if any.
     */
    @ScxRoute(value = "", methods = HttpMethod.GET, order = 10)
    public Html TestIndex(RoutingContext c) throws IOException {
        System.err.println("最后一次匹配的路由" + c.request().path());
        Html index = Html.of("index");
        index.add("name", c.get("name"));
        index.add("age", 22);
        return index;
    }

    /**
     * 多个路由
     *
     * @param c a
     * @throws IOException a
     */
    @ScxRoute(value = "", methods = HttpMethod.GET, order = 5)
    public void TestIndex1(RoutingContext c) throws IOException {
        System.err.println("第二个匹配的路由" + c.request().path());
        c.put("name", "小明");
        c.next();
    }

    /**
     * 这里如果 order 小于其他的 order 根据 其会因其路径为(模糊路径) 在最后进行才进行匹配
     *
     * @param c a
     * @throws IOException a
     */
    @ScxRoute(value = "/*", methods = HttpMethod.GET, order = 1)
    public void TestIndex1a(RoutingContext c) throws IOException {
        System.err.println("两个 carService 是否相等 " + (carService == carService1));
        System.err.println("第一个匹配的路由" + c.request().path());
        c.next();
    }

    /**
     * 测试!!!
     *
     * @return a
     */
    @ScxRoute(value = "/baidu", methods = HttpMethod.GET)
    public Html TestHttpUtils() throws IOException, InterruptedException {
        var baiduHtml = ScxHttpClientHelper.get("https://www.baidu.com/").body().toString();
        return Html.ofString(baiduHtml);
    }

    /**
     * 测试!!!
     *
     * @return a
     */
    @ScxRoute(value = "/download", methods = HttpMethod.GET)
    public Download TestDownload() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 9999; i = i + 1) {
            s.append("这是文字 ").append(i).append(", ");
        }
        return Download.of(s.toString().getBytes(StandardCharsets.UTF_8), "测试中 + - ~!文 a😊😂 🤣 ghj ❤😍😒👌.txt");
    }

    /**
     * 测试!!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxRoute(value = "/raw", methods = HttpMethod.GET)
    public BaseVo TestRaw() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 9999; i = i + 1) {
            s.append("这是文字 ").append(i).append(", ");
        }
        return Raw.of(s.toString().getBytes(StandardCharsets.UTF_8), FileFormat.TXT);
    }

    /**
     * 测试!!!
     *
     * @return a {@link java.lang.String} object
     */
    @ScxRoute(value = "/md5", methods = HttpMethod.GET)
    public String TestMd5() {
        return HashUtils.md5Hex("123");
    }

    /**
     * 测试!!!
     *
     * @return a {@link java.lang.String} object
     */
    @ScxRoute(methods = HttpMethod.GET)
    public String getRandomCode() {
        return RandomUtils.randomString(9999);
    }

    /**
     * 测试!!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxRoute(methods = HttpMethod.GET)
    public BaseVo bigJson() {
        var users = carService1.find();
        return Result.ok().put("items", users);
    }

    @ScxRoute(methods = HttpMethod.GET)
    public BaseVo bigXml() {
        var users = carService1.find();
        return Xml.of(users);
    }

    /**
     * 测试!!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxRoute(methods = HttpMethod.GET)
    public BaseVo a() {
        return Result.ok().put("items", "a");
    }

    /**
     * 测试!!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxRoute(value = "a", methods = HttpMethod.GET)
    public BaseVo b() {
        return Result.ok().put("items", "b");
    }

    /**
     * 测试 重复路由 !!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxRoute(value = "/v/:aaa", methods = {HttpMethod.GET, HttpMethod.POST})
    public BaseVo c() {
        return Result.ok().put("items", "aaa");
    }

    /**
     * 测试 重复路由 !!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxRoute(value = "/v/:bbb", methods = HttpMethod.GET)
    public BaseVo d() {
        return Result.ok().put("items", "bbb");
    }

    /**
     * <p>qrcode.</p>
     *
     * @return a {@link BaseVo} object
     */
    @ScxRoute(methods = HttpMethod.GET)
    public Download zip() throws Exception {
        var zipBuilder = new ZipBuilder();
        zipBuilder.put("第一个目录/第二个目录/第二个目录中的文件.txt", "文件内容".getBytes(StandardCharsets.UTF_8));
        zipBuilder.put("第一个目录/这是一系列空目录/这是一系列空目录/这是一系列空目录/这是一系列空目录/这是一系列空目录");
        zipBuilder.put("第一个目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录");
        zipBuilder.put("第一个目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/一个文本文件.txt", "一些内容,一些内容,一些内容,一些内容 下😊😂🤣❤😍😒👌😘".getBytes(StandardCharsets.UTF_8));
        zipBuilder.put("第三个目录/子目录");
        zipBuilder.remove("第三个目录");

        // 大型文件请使用这种方法下载
        var in = new PipedInputStream();
        var out = new PipedOutputStream(in);

        new Thread(() -> ScxExceptionHelper.wrap(() -> {
            try (var zos = new ZipOutputStream(out)) {
                zipBuilder.writeToZipOutputStream(zos);
            }
        })).start();

        return Download.of(in, "测试压缩包.zip");

    }

}

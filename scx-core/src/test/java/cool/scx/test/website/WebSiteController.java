package cool.scx.test.website;

import cool.scx.core.ScxConstant;
import cool.scx.core.ScxContext;
import cool.scx.core.annotation.FromQuery;
import cool.scx.core.annotation.FromUpload;
import cool.scx.core.annotation.ScxMapping;
import cool.scx.core.type.UploadedEntity;
import cool.scx.core.vo.*;
import cool.scx.enumeration.HttpMethod;
import cool.scx.enumeration.RawType;
import cool.scx.http_client.ScxHttpClientHelper;
import cool.scx.test.car.Car;
import cool.scx.test.car.CarService;
import cool.scx.util.DigestUtils;
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

/**
 * 简单测试
 *
 * @author scx567888
 * @version 0.3.6
 * @since 1.3.14
 */
@ScxMapping
public class WebSiteController {

    final CarService carService;

    @Autowired
    CarService carService1;

    public WebSiteController(CarService carService) {
        this.carService = carService;
    }

    @ScxMapping(method = HttpMethod.POST)
    public static Object test0(@FromQuery String name,
                               @FromQuery Integer age,
                               @FromUpload UploadedEntity content,
                               @FromUpload FileUpload content1) {
        System.err.println("客户端 IP :" + NetUtils.getClientIPAddress(ScxContext.routingContext().request()));
        return Map.of("now", ScxConstant.DEFAULT_DATETIME_FORMATTER.format(LocalDateTime.now()),
                "name", name, "age", age, "content", content.buffer().toString(StandardCharsets.UTF_8),
                "content1", ScxContext.vertx().fileSystem().readFileBlocking(content1.uploadedFileName()).toString(StandardCharsets.UTF_8));
    }

    @ScxMapping(method = HttpMethod.GET)
    public static void TestTransaction(RoutingContext ctx) throws Exception {
        var sb = new StringBuilder();
        CarService bean = ScxContext.getBean(CarService.class);
        try {
            ScxContext.autoTransaction(() -> {
                sb.append("事务开始前数据库中 数据条数 : ").append(bean.list().size()).append("</br>");
                sb.append("现在插入 1 数据条数").append("</br>");
                var u = new Car();
                u.name = "唯一name";
                bean.add(u);
                sb.append("现在数据库中数据条数 : ").append(bean.list().size()).append("</br>");
                bean.add(u);
            });
        } catch (Exception e) {
            sb.append("出错了 后滚后数据库中数据条数 : ").append(bean.list().size());
        }
        Html.ofString(sb.toString()).accept(ctx);
    }

    /**
     * 测试!!!
     *
     * @return 页面
     * @throws java.io.IOException if any.
     */
    @ScxMapping(value = "/", method = HttpMethod.GET, order = 10)
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
    @ScxMapping(value = "/", method = HttpMethod.GET, order = 5)
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
    @ScxMapping(value = "/*", method = HttpMethod.GET, order = 1)
    public void TestIndex1a(RoutingContext c) throws IOException {
        System.err.println("两个 carService 是否相等 " + (carService == carService1));
        System.err.println("第一个匹配的路由" + c.request().path());
        c.next();
    }

    /**
     * 测试!!!
     *
     * @return a {@link cool.scx.core.vo.Html} object
     */
    @ScxMapping(value = "/baidu", method = HttpMethod.GET)
    public Html TestHttpUtils() throws IOException, InterruptedException {
        var baiduHtml = ScxHttpClientHelper.get("https://www.baidu.com/").body().toString();
        return Html.ofString(baiduHtml);
    }

    /**
     * 测试!!!
     *
     * @return a {@link cool.scx.core.vo.Download} object
     */
    @ScxMapping(value = "/download", method = HttpMethod.GET)
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
    @ScxMapping(value = "/raw", method = HttpMethod.GET)
    public BaseVo TestRaw() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 9999; i = i + 1) {
            s.append("这是文字 ").append(i).append(", ");
        }
        return Raw.of(s.toString().getBytes(StandardCharsets.UTF_8), RawType.TXT);
    }

    /**
     * 测试!!!
     *
     * @return a {@link java.lang.String} object
     */
    @ScxMapping(value = "/md5", method = HttpMethod.GET)
    public String TestMd5() {
        return DigestUtils.md5("123");
    }

    /**
     * 测试!!!
     *
     * @return a {@link java.lang.String} object
     */
    @ScxMapping(method = HttpMethod.GET)
    public String getRandomCode() {
        return RandomUtils.randomString(9999);
    }

    /**
     * 测试!!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxMapping(method = HttpMethod.GET)
    public BaseVo bigJson() {
        var users = carService1.list();
        return Json.ok().put("items", users);
    }

    @ScxMapping(method = HttpMethod.GET)
    public BaseVo bigXml() {
        var users = carService1.list();
        return Xml.of(users);
    }

    /**
     * 测试!!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxMapping(method = HttpMethod.GET)
    public BaseVo a() {
        return Json.ok().put("items", "a");
    }

    /**
     * 测试!!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxMapping(value = "a", method = HttpMethod.GET)
    public BaseVo b() {
        return Json.ok().put("items", "b");
    }

    /**
     * 测试 重复路由 !!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxMapping(value = "/v/:aaa", method = {HttpMethod.GET, HttpMethod.POST})
    public BaseVo c() {
        return Json.ok().put("items", "aaa");
    }

    /**
     * 测试 重复路由 !!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxMapping(value = "/v/:bbb", method = HttpMethod.GET)
    public BaseVo d() {
        return Json.ok().put("items", "bbb");
    }

    /**
     * <p>qrcode.</p>
     *
     * @return a {@link BaseVo} object
     */
    @ScxMapping(method = HttpMethod.GET)
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

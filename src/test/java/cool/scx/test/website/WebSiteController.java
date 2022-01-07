package cool.scx.test.website;

import cool.scx.ScxContext;
import cool.scx.annotation.ScxMapping;
import cool.scx.enumeration.HttpMethod;
import cool.scx.enumeration.RawType;
import cool.scx.test.car.Car;
import cool.scx.test.car.CarService;
import cool.scx.util.RandomUtils;
import cool.scx.util.digest.DigestUtils;
import cool.scx.util.http.HttpClientHelper;
import cool.scx.util.zip.IVirtualFile;
import cool.scx.util.zip.VirtualDirectory;
import cool.scx.util.zip.VirtualFile;
import cool.scx.util.zip.ZipAction;
import cool.scx.vo.*;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

    @ScxMapping(method = HttpMethod.GET)
    public static void TestTransaction(RoutingContext ctx) throws Exception {
        var sb = new StringBuilder();
        CarService bean = ScxContext.getBean(CarService.class);
        try {
            ScxContext.sqlRunner().autoTransaction((con) -> {
                sb.append("事务开始前数据库中 数据条数 : ").append(bean.list(con).size()).append("</br>");
                sb.append("现在插入 1 数据条数").append("</br>");
                var u = new Car();
                u.name = "唯一name";
                bean.save(con, u);
                sb.append("现在数据库中数据条数 : ").append(bean.list(con).size()).append("</br>");
                bean.save(con, u);
            });
        } catch (Exception e) {
            sb.append("出错了 后滚后数据库中数据条数 : ").append(bean.list().size());
        }
        Html.ofString(sb.toString()).handle(ctx);
    }

    /**
     * 测试!!!
     *
     * @return 页面
     * @throws java.io.IOException if any.
     */
    @ScxMapping(value = "/", method = HttpMethod.GET)
    public Html TestIndex() throws IOException {
        System.err.println("两个 carService 是否相等 " + (carService == carService1));
        Html index = Html.of("index");
        index.add("name", "小明");
        index.add("age", 22);
        return index;
    }

    /**
     * 测试!!!
     *
     * @return a {@link cool.scx.vo.Html} object
     */
    @ScxMapping(value = "/baidu", method = HttpMethod.GET)
    public Html TestHttpUtils() throws IOException, InterruptedException {
        var baiduHtml = HttpClientHelper.get("https://www.baidu.com/").body();
        return Html.ofString(baiduHtml);
    }

    /**
     * 测试!!!
     *
     * @return a {@link cool.scx.vo.Download} object
     */
    @ScxMapping(value = "/download", method = HttpMethod.GET)
    public Download TestDownload() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 9999; i++) {
            s.append("这是文字 ").append(i).append(", ");
        }
        return new Download(s.toString().getBytes(StandardCharsets.UTF_8), "测试中 + - ~!文 a😊😂 🤣 ghj ❤😍😒👌.txt");
    }

    /**
     * 测试!!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxMapping(value = "/raw", method = HttpMethod.GET)
    public BaseVo TestRaw() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 9999; i++) {
            s.append("这是文字 ").append(i).append(", ");
        }
        return new Raw(s.toString().getBytes(StandardCharsets.UTF_8), RawType.TXT);
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
        return RandomUtils.getRandomString(9999, true);
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
        return new Xml(users);
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
    @ScxMapping(value = "/v/:aaa", method = HttpMethod.GET)
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
    public BaseVo zip() throws Exception {
        var virtualDirectory = VirtualDirectory.of("第一个目录");
        virtualDirectory.put("第二个目录", VirtualFile.of("第二个目录中的文件.txt", "文件内容".getBytes(StandardCharsets.UTF_8)));
        virtualDirectory.getOrCreate("这是一系列空目录/这是一系列空目录/这是一系列空目录/这是一系列空目录/这是一系列空目录");
        IVirtualFile orCreate = virtualDirectory.getOrCreate("这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录/这不是一系列空目录");
        if (orCreate instanceof VirtualDirectory a) {
            a.put(VirtualFile.of("一个文本文件.txt", "一些内容,一些内容,一些内容,一些内容 下😊😂🤣❤😍😒👌😘".getBytes(StandardCharsets.UTF_8)));
        }
        byte[] bytes = ZipAction.toZipFileByteArray(virtualDirectory);
        return new Download(bytes, "测试压缩包.zip");
    }

}

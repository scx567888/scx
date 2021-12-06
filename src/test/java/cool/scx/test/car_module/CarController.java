package cool.scx.test.car_module;

import cool.scx.annotation.ScxMapping;
import cool.scx.enumeration.HttpMethod;
import cool.scx.enumeration.RawType;
import cool.scx.util.HttpUtils;
import cool.scx.util.RandomUtils;
import cool.scx.util.digest.DigestUtils;
import cool.scx.vo.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 简单测试
 *
 * @author scx567888
 * @version 0.3.6
 * @since 1.3.14
 */
@ScxMapping
public class CarController {

    /**
     * 测试!!!
     *
     * @return 页面
     * @throws java.io.IOException if any.
     */
    @ScxMapping(value = "/", method = HttpMethod.GET)
    public Html TestIndex() throws IOException {
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
        HttpResponse<String> stringHttpResponse = HttpUtils.get("https://www.baidu.com/", new HashMap<>());
        return Html.ofString(stringHttpResponse.body());
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
            s.append("这是文字 ").append(i);
        }
        return new Download(s.toString().getBytes(StandardCharsets.UTF_8), "测试中 + - ~!文 a😊😂 🤣 ghj ❤😍😒👌.txt");
    }

    /**
     * 测试!!!
     *
     * @return a {@link BaseVo} object
     */
    @ScxMapping(value = "/raw", method = HttpMethod.GET)
    public BaseVo TestBinary() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 9999; i++) {
            s.append("这是文字").append(i);
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

}

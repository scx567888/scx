package cool.scx.http.test;

import cool.scx.http.headers.ScxHttpHeaders;
import org.testng.annotations.Test;

import static cool.scx.http.headers.HttpFieldName.CONTENT_LENGTH;
import static cool.scx.http.headers.HttpFieldName.CONTENT_TYPE;

public class ScxHttpHeadersTest {

    public static void main(String[] args) {
        test1();
        test2();
    }

    @Test
    public static void test1() {
        var headers = ScxHttpHeaders.of();
        headers.set("content-type", "text/html");
        headers.add(CONTENT_TYPE, "application/json");
        headers.add(CONTENT_LENGTH, "100");
        headers.add("abc", "456");
        headers.remove("abc");
        System.out.println(headers.encode());
    }

    @Test
    public static void test2() {
        //在兼容模式下 允许同时存在两种换行
        var h = ScxHttpHeaders.of("""
                a:b\r
                c: d
                e:   f\r
                g: h
                """);
        System.out.println(h);

        //在严格模式下 我们的 值是可以包含单独的 \n 而不导致解析错误的 虽然这种头格式并不合法
        var h1 = ScxHttpHeaders.ofStrict("""
                a:换行
                    b\r
                c: 换行
                      d\r
                e:   f\r
                """);

        System.out.println(h1);
    }

}

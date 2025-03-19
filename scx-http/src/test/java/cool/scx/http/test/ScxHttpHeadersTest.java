package cool.scx.http.test;

import cool.scx.http.headers.ScxHttpHeaders;
import org.testng.annotations.Test;

import static cool.scx.http.headers.HttpFieldName.CONTENT_LENGTH;
import static cool.scx.http.headers.HttpFieldName.CONTENT_TYPE;
import static cool.scx.http.headers.ScxHttpHeadersHelper.encodeHeaders;
import static cool.scx.http.headers.ScxHttpHeadersHelper.parseHeaders;

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
        for (var header : headers) {
            System.out.println(encodeHeaders(headers));
        }
    }

    @Test
    public static void test2() {
        var h = parseHeaders(ScxHttpHeaders.of(), """
                a:b\r
                c: d\r
                e:   f\r
                """);
        for (var c : h) {
            System.out.println(c);
        }

    }

}

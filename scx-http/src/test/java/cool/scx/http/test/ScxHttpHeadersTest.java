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
        var h = ScxHttpHeaders.of("""
                a:b
                c: d
                e:   f
                """);
        for (var c : h) {
            System.out.println(c);
        }
    }

}

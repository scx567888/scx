package cool.scx.http.test;

import cool.scx.http.media_type.MediaType;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.content_type.ContentType;

import java.nio.charset.StandardCharsets;

public class HeadersTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        long l = System.nanoTime();
        for (int i = 0; i < 9999; i = i + 1) {
            var h = ScxHttpHeaders.of();
            h.add("Content-Disposition", "form-data; name=myname");
            h.contentLength(100);
            h.contentType(ContentType.of(MediaType.APPLICATION_JSON).charset(StandardCharsets.UTF_8));
            var s = h.encode();
            var nw = ScxHttpHeaders.of(s);
        }
        System.out.println((System.nanoTime() - l) / 1000_000);
    }

}

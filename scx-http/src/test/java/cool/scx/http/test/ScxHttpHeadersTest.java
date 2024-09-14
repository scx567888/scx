package cool.scx.http.test;

import cool.scx.http.ScxHttpHeaders;

import static cool.scx.http.HttpFieldName.CONTENT_LENGTH;
import static cool.scx.http.HttpFieldName.CONTENT_TYPE;

public class ScxHttpHeadersTest {

    public static void main(String[] args) {
        var headers = ScxHttpHeaders.of();
        headers.set("content-type", "text/html");
        headers.add(CONTENT_TYPE, "application/json");
        headers.add(CONTENT_LENGTH, "100");
        headers.add("abc", "456");
        headers.remove("abc");
        for (var header : headers) {
            System.out.println(header);
        }
    }

}

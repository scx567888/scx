package cool.scx.http.x.test;

import cool.scx.http.uri.ScxURI;
import cool.scx.http.x.http1.Http1RequestLine;
import org.testng.Assert;

import static cool.scx.http.HttpMethod.GET;
import static org.testng.Assert.assertEquals;

public class Http1RequestLineTest {

    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {

        //这是正确的
        var http1RequestLine = Http1RequestLine.of("GET /foo HTTP/1.1");

        //这里是非法http版本
        try {
            var requestLine = Http1RequestLine.of("GET /foo HTTP/1.3");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Invalid HTTP version : HTTP/1.3");
        }

        //这里是 Http/0.9 理论上应该抛出 400 
        try {
            var requestLine = Http1RequestLine.of("GET /foo");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Invalid HTTP request line : GET /foo");
        }

        //这里是 多余空格 理论上应该抛出 400 
        try {
            var requestLine = Http1RequestLine.of("GET /foo abc HTTP/1.1");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Invalid HTTP request line : GET /foo abc HTTP/1.1");
        }

        //这里是 不可解析的路径 理论上应该抛出 400 
        try {
            var requestLine = Http1RequestLine.of("GET /% HTTP/1.1");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Invalid HTTP request line : GET /% HTTP/1.1");
        }

    }

    public static void test2() {
        var http1RequestLine = new Http1RequestLine(GET, ScxURI.of().path("/中文/bar").addQuery("aaa", "bbb")).encode();
        assertEquals(http1RequestLine, "GET /%E4%B8%AD%E6%96%87/bar?aaa=bbb HTTP/1.1");
    }

}

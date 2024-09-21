package cool.scx.http.test;

import cool.scx.http.ScxURI;

public class URITest {

    public static void main(String[] args) {
        var uri = ScxURI.of("http://www.example.com/abc/bcd?name=小明&age=20#999");
        System.out.println(uri);
    }

}

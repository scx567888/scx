package cool.scx.http.test;

import cool.scx.http.routing.PathMatcher;

public class PathMatcherTest {

    public static void main(String[] args) {
        var matcher = PathMatcher.of("/a/b/:id");
        var result = matcher.matches("/a/b/苹果");
        var result1 = matcher.matches("/a/b/ 空格 ");
        var result2 = matcher.matches("/a/b/c/ ");
        System.out.println(result.accepted() + " " + result.pathParams().get("id"));
        System.out.println(result1.accepted() + " " + result1.pathParams().get("id"));
        System.out.println(result2.accepted() + " " + result2.pathParams().get("id"));

        var matcher2 = PathMatcher.of("/a/b/*");
        var result5 = matcher2.matches("/a/b/");
        var result6 = matcher2.matches("/a/b/c/d/f/e/f");
        System.out.println(result5.accepted() + " " + result5.pathParams().get("*"));
        System.out.println(result6.accepted() + " " + result6.pathParams().get("*"));

        var matcher3 = PathMatcher.of("/a/b/:name/*");
        var result7 = matcher3.matches("/a/b/小明/");
        var result8 = matcher3.matches("/a/b/小明/9");
        System.out.println(result7.accepted() + " " + result7.pathParams().get("*"));
        System.out.println(result7.accepted() + " " + result7.pathParams().get("name"));
        System.out.println(result8.accepted() + " " + result8.pathParams().get("*"));
        System.out.println(result8.accepted() + " " + result8.pathParams().get("name"));
    }

}

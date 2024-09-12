package cool.scx.http.test;

import cool.scx.http.PathMatcher;

public class PathMatcherTest {

    public static void main(String[] args) {
        var matcher = PathMatcher.ofPath("/a/b/:id");
        var result = matcher.matches("/a/b/苹果");
        var result1 = matcher.matches("/a/b/ 空格 ");
        var result2 = matcher.matches("/a/b/c/ ");
        var matcher2 = PathMatcher.ofPath("/a/b/*");
        var result5 = matcher2.matches("/a/b/");
        var result6 = matcher2.matches("/a/b/c/d/f/e/f");
        System.out.println(result.accepted() + " " + result.params().get("id"));
        System.out.println(result1.accepted() + " " + result1.params().get("id"));
        System.out.println(result2.accepted() + " " + result2.params().get("id"));
        System.out.println(result5.accepted() + " " + result5.params().get("*"));
        System.out.println(result6.accepted() + " " + result6.params().get("*"));
    }

}

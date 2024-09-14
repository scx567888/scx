package cool.scx.http.test;

import cool.scx.http.ScxHttpMethod;
import cool.scx.http.routing.MethodMatcher;

import static cool.scx.http.HttpMethod.GET;
import static cool.scx.http.HttpMethod.POST;

public class MethodMatcherTest {

    public static void main(String[] args) {
        var matcher = MethodMatcher.of(GET, POST);
        var result = matcher.matches(ScxHttpMethod.of("get"));
        System.out.println(result);
    }

}

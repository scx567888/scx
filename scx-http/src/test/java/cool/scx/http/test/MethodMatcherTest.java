package cool.scx.http.test;

import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.routing.MethodMatcher;

import static cool.scx.http.method.HttpMethod.GET;
import static cool.scx.http.method.HttpMethod.POST;

public class MethodMatcherTest {

    public static void main(String[] args) {
        var matcher = MethodMatcher.of(GET, POST);
        var result = matcher.matches(ScxHttpMethod.of("GET"));
        System.out.println(result);
    }

}

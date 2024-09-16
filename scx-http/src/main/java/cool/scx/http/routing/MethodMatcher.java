package cool.scx.http.routing;

import cool.scx.http.ScxHttpMethod;

public interface MethodMatcher {

    static MethodMatcher any() {
        return method -> true;
    }

    static MethodMatcher of(ScxHttpMethod... method) {
        return new MethodMatcherImpl(method);
    }

    boolean matches(ScxHttpMethod method);

}

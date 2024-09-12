package cool.scx.http;

public interface MethodMatcher {

    static MethodMatcher of() {
        return new MethodMatcherImpl();
    }

    static MethodMatcher of(ScxHttpMethod... method) {
        return new MethodMatcherImpl(method);
    }

    boolean matches(ScxHttpMethod method);

}

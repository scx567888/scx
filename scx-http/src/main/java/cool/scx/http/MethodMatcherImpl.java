package cool.scx.http;

import java.util.Set;

public class MethodMatcherImpl implements MethodMatcher {

    private final Set<ScxHttpMethod> methods;

    MethodMatcherImpl(ScxHttpMethod... methods) {
        this.methods = Set.of(methods);
    }

    @Override
    public boolean matches(ScxHttpMethod method) {
        return methods.contains(method);
    }

}
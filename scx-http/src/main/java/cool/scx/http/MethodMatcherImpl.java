package cool.scx.http;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MethodMatcherImpl implements MethodMatcher {

    private final Set<ScxHttpMethod> methods;

    MethodMatcherImpl() {
        this.methods = null;
    }

    MethodMatcherImpl(ScxHttpMethod... methods) {
        this.methods = new HashSet<>();
        this.methods.addAll(Arrays.asList(methods));
    }

    @Override
    public boolean matches(ScxHttpMethod method) {
        if (methods == null) {
            return true;
        } else {
            return methods.contains(method);
        }
    }

}

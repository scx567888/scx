package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;

import static cool.scx.http.routing.TypeMatcherImpl.ANY_TYPE;

public interface TypeMatcher {

    static TypeMatcher any() {
        return ANY_TYPE;
    }

    boolean matches(ScxHttpServerRequest request);

}

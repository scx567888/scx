package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;

public interface TypeMatcher {

    TypeMatcher ANY = _ -> true;

    static TypeMatcher any() {
        return ANY;
    }

    boolean matches(ScxHttpServerRequest request);

}

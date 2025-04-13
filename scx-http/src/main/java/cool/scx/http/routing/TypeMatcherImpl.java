package cool.scx.http.routing;

import cool.scx.http.ScxHttpServerRequest;

public class TypeMatcherImpl implements TypeMatcher {

    static TypeMatcher ANY_TYPE = new TypeMatcherImpl();

    @Override
    public boolean matches(ScxHttpServerRequest request) {
        return true;
    }

}

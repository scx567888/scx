package cool.scx.http.helidon;

import io.helidon.common.parameters.Parameters;
import io.helidon.http.HttpPrologue;
import io.helidon.http.RoutedPath;

class HelidonRoutePath implements RoutedPath {

    private final HttpPrologue p;

    public HelidonRoutePath(HttpPrologue p) {
        this.p = p;
    }

    @Override
    public Parameters pathParameters() {
        return p.query();
    }

    @Override
    public RoutedPath absolute() {
        return this;
    }

    @Override
    public String rawPath() {
        return p.uriPath().rawPath();
    }

    @Override
    public String rawPathNoParams() {
        return "";
    }

    @Override
    public String path() {
        return p.uriPath().path();
    }

    @Override
    public Parameters matrixParameters() {
        return null;
    }

    @Override
    public void validate() {

    }

}

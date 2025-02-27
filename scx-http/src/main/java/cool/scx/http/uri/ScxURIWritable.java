package cool.scx.http.uri;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

import java.util.Arrays;

import static cool.scx.http.uri.ScxURIHelper.decodeQuery;

/// ScxURIWritable
///
/// @author scx567888
/// @version 0.0.1
public interface ScxURIWritable extends ScxURI {

    @Override
    ParametersWritable<String, String> query();

    ScxURIWritable scheme(String scheme);

    ScxURIWritable host(String host);

    ScxURIWritable port(int port);

    ScxURIWritable path(String path);

    ScxURIWritable query(Parameters<String, String> query);

    ScxURIWritable fragment(String fragment);

    default ScxURIWritable query(String queryString) {
        return query(decodeQuery(queryString));
    }

    default ScxURIWritable setQuery(String name, String... value) {
        query().set(name, value);
        return this;
    }

    default ScxURIWritable addQuery(String name, String... value) {
        query().add(name, value);
        return this;
    }

    default ScxURIWritable addQuery(String name, Object... value) {
        query().add(name, Arrays.stream(value).map(Object::toString).toArray(String[]::new));
        return this;
    }

    default ScxURIWritable removeQuery(String name) {
        query().remove(name);
        return this;
    }

    default ScxURIWritable clearQuery() {
        query().clear();
        return this;
    }

}

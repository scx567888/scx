package cool.scx.http;

import static cool.scx.http.URIHelper.decodeQuery;

/**
 * ScxURIWritable
 */
public interface ScxURIWritable extends ScxURI {

    @Override
    ParametersWritable<String, String> query();

    ScxURIWritable scheme(String scheme);

    ScxURIWritable host(String host);

    ScxURIWritable port(int port);

    ScxURIWritable path(String path);

    ScxURIWritable query(ParametersWritable<String, String> query);

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

    default ScxURIWritable removeQuery(String name) {
        query().remove(name);
        return this;
    }

}

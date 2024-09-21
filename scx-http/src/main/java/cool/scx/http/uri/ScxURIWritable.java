package cool.scx.http.uri;

import cool.scx.http.ParametersWritable;

import static cool.scx.http.uri.URIHelper.decodeQuery;

/**
 * ScxURIWritable
 */
public interface ScxURIWritable extends ScxURI {

    @Override
    URIPathWritable path();

    @Override
    ParametersWritable<String, String> query();

    @Override
    URIFragmentWritable fragment();

    ScxURIWritable scheme(String scheme);

    ScxURIWritable host(String host);

    ScxURIWritable port(int port);

    ScxURIWritable path(URIPathWritable path);

    ScxURIWritable query(ParametersWritable<String, String> query);

    ScxURIWritable fragment(URIFragmentWritable fragment);

    default ScxURIWritable path(String path) {
        return path(URIPath.of(path));
    }

    default ScxURIWritable query(String queryString) {
        return query(decodeQuery(queryString));
    }

    default ScxURIWritable fragment(String fragment) {
        return fragment(URIFragment.of(fragment));
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

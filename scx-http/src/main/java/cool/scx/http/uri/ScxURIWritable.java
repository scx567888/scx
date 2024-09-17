package cool.scx.http.uri;

/**
 * ScxURIWritable
 */
public interface ScxURIWritable extends ScxURI {

    @Override
    URIPathWritable path();

    @Override
    URIQueryWritable query();

    @Override
    URIFragmentWritable fragment();

    ScxURIWritable scheme(String scheme);

    ScxURIWritable host(String host);

    ScxURIWritable port(int port);

    ScxURIWritable path(URIPathWritable path);

    ScxURIWritable query(URIQueryWritable query);

    ScxURIWritable fragment(URIFragmentWritable fragment);

    default ScxURIWritable path(String path) {
        return path(URIPath.of(path));
    }

    default ScxURIWritable query(String query) {
        return query(URIQuery.of(query));
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

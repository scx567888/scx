package cool.scx.http.uri;

/**
 * URIWritable
 */
public interface URIWritable extends URI {

    @Override
    URIPathWritable path();

    @Override
    URIQueryWritable query();

    @Override
    URIFragmentWritable fragment();

    URIWritable scheme(String scheme);

    URIWritable host(String host);

    URIWritable port(int port);

    URIWritable path(URIPathWritable path);

    URIWritable query(URIQueryWritable query);

    URIWritable fragment(URIFragmentWritable fragment);

    default URIWritable path(String path) {
        return path(URIPath.of(path));
    }

    default URIWritable query(String query) {
        return query(URIQuery.of(query));
    }

    default URIWritable fragment(String fragment) {
        return fragment(URIFragment.of(fragment));
    }

    default URIWritable setQuery(String name, String... value) {
        query().set(name, value);
        return this;
    }

    default URIWritable addQuery(String name, String... value) {
        query().add(name, value);
        return this;
    }

    default URIWritable removeQuery(String name) {
        query().remove(name);
        return this;
    }

}

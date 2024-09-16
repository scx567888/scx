package cool.scx.http.uri;

public interface URIWritable extends URI {

    URIWritable scheme(String scheme);

    URIWritable host(String host);

    URIWritable port(int port);

    URIWritable path(URIPath path);

    URIWritable query(URIQuery query);

    URIWritable fragment(URIFragment fragment);

    default URIWritable path(String path) {
        return path(URIPath.of(path));
    }

    default URIWritable query(String query) {
        return query(URIQuery.of(query));
    }

    default URIWritable fragment(String fragment) {
        return fragment(URIFragment.of(fragment));
    }

}

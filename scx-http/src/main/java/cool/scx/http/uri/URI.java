package cool.scx.http.uri;

public interface URI {

    static URIWritable of() {
        return new URIImpl();
    }

    static URIWritable of(String uri) {
        var u = java.net.URI.create(uri);
        return new URIImpl()
                .scheme(u.getScheme())
                .host(u.getHost())
                .port(u.getPort())
                .path(URIPath.of(u.getPath()))
                .query(URIQuery.of(u.getQuery()))
                .fragment(URIFragment.of(u.getFragment()));
    }

    String scheme();

    String host();

    int port();

    URIPath path();

    URIQuery query();

    URIFragment fragment();

}

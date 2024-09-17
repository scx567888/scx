package cool.scx.http.uri;

/**
 * ScxURI
 */
public interface ScxURI {

    static ScxURIWritable of() {
        return new ScxURIImpl();
    }

    static ScxURIWritable of(String uri) {
        var u = java.net.URI.create(uri);
        return new ScxURIImpl()
                .scheme(u.getScheme())
                .host(u.getHost())
                .port(u.getPort())
                .path(u.getPath())
                .query(u.getQuery())
                .fragment(u.getFragment());
    }

    String scheme();

    String host();

    int port();

    URIPath path();

    URIQuery query();

    URIFragment fragment();

}

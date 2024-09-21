package cool.scx.http.uri;

import cool.scx.http.Parameters;

import java.net.URI;

/**
 * ScxURI
 */
public interface ScxURI {

    static ScxURIWritable of() {
        return new ScxURIImpl();
    }

    static ScxURIWritable of(String uri) {
        return of(URI.create(uri));
    }

    static ScxURIWritable of(URI u) {
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

    String path();

    Parameters<String, String> query();

    String fragment();

    default String getQuery(String name) {
        return query().get(name);
    }

}

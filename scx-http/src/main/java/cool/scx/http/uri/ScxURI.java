package cool.scx.http.uri;

import cool.scx.http.Parameters;

import java.net.URI;
import java.net.URISyntaxException;

import static cool.scx.http.uri.ScxURIHelper.encodeQuery;
import static cool.scx.http.uri.ScxURIHelper.parseURI;

/**
 * ScxURI
 */
public interface ScxURI {

    static ScxURIWritable of() {
        return new ScxURIImpl();
    }

    static ScxURIWritable of(String uri) {
        return of(parseURI(uri));
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

    /**
     * 注意此编码会进行 URL编码 转换
     *
     * @return a
     */
    default String encode() {
        return encode(false);
    }

    default String encode(boolean uriEncoding) {
        return ScxURIHelper.encodeURI(this, uriEncoding);
    }

    default URI toURI() throws URISyntaxException {
        return new URI(scheme(), null, host(), port(), path(), encodeQuery(query(), false), fragment());
    }

}

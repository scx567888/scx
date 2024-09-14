package cool.scx.http.uri;

public class URIImpl implements URIWritable {

    private String scheme;
    private String host;
    private int port;
    private URIPath path;
    private URIQuery query;
    private URIFragment fragment;

    @Override
    public URIWritable scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    @Override
    public URIWritable host(String host) {
        this.host = host;
        return this;
    }

    @Override
    public URIWritable port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public URIWritable path(URIPath path) {
        this.path = path;
        return this;
    }

    @Override
    public URIWritable query(URIQuery query) {
        this.query = query;
        return this;
    }

    @Override
    public URIWritable fragment(URIFragment fragment) {
        this.fragment = fragment;
        return this;
    }

    @Override
    public String scheme() {
        return scheme;
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public URIPath path() {
        return path;
    }

    @Override
    public URIQuery query() {
        return query;
    }

    @Override
    public URIFragment fragment() {
        return fragment;
    }

}

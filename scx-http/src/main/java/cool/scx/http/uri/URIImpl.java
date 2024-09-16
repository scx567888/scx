package cool.scx.http.uri;

/**
 * URIImpl
 */
public class URIImpl implements URIWritable {

    private String scheme;
    private String host;
    private int port;
    private URIPathWritable path;
    private URIQueryWritable query;
    private URIFragmentWritable fragment;

    public URIImpl() {
        this.scheme = null;
        this.host = null;
        this.port = -1;
        this.path = URIPath.of();
        this.query = URIQuery.of();
        this.fragment = URIFragment.of();
    }

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
    public URIWritable path(URIPathWritable path) {
        this.path = path;
        return this;
    }

    @Override
    public URIWritable query(URIQueryWritable query) {
        this.query = query;
        return this;
    }

    @Override
    public URIWritable fragment(URIFragmentWritable fragment) {
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
    public URIPathWritable path() {
        return path;
    }

    @Override
    public URIQueryWritable query() {
        return query;
    }

    @Override
    public URIFragmentWritable fragment() {
        return fragment;
    }

    @Override
    public String toString() {
        return URIHelper.toString(this);
    }

}

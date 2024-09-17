package cool.scx.http.uri;

/**
 * ScxURIImpl
 */
public class ScxURIImpl implements ScxURIWritable {

    private String scheme;
    private String host;
    private int port;
    private URIPathWritable path;
    private URIQueryWritable query;
    private URIFragmentWritable fragment;

    public ScxURIImpl() {
        this.scheme = null;
        this.host = null;
        this.port = -1;
        this.path = URIPath.of();
        this.query = URIQuery.of();
        this.fragment = URIFragment.of();
    }

    @Override
    public ScxURIWritable scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    @Override
    public ScxURIWritable host(String host) {
        this.host = host;
        return this;
    }

    @Override
    public ScxURIWritable port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public ScxURIWritable path(URIPathWritable path) {
        this.path = path;
        return this;
    }

    @Override
    public ScxURIWritable query(URIQueryWritable query) {
        this.query = query;
        return this;
    }

    @Override
    public ScxURIWritable fragment(URIFragmentWritable fragment) {
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

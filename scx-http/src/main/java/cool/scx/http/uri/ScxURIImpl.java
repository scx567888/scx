package cool.scx.http.uri;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

/**
 * ScxURIImpl
 */
public class ScxURIImpl implements ScxURIWritable {

    private String scheme;
    private String host;
    private int port;
    private URIPathWritable path;
    private ParametersWritable<String, String> query;
    private URIFragmentWritable fragment;

    public ScxURIImpl() {
        this.scheme = null;
        this.host = null;
        this.port = -1;
        this.path = URIPath.of();
        this.query = Parameters.of();
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
    public ScxURIWritable query(ParametersWritable<String, String> query) {
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
    public ParametersWritable<String, String> query() {
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

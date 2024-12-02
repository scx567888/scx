package cool.scx.http.uri;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

/**
 * ScxURIImpl
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ScxURIImpl implements ScxURIWritable {

    private String scheme;
    private String host;
    private int port;
    private String path;
    private ParametersWritable<String, String> query;
    private String fragment;

    public ScxURIImpl() {
        this.scheme = null;
        this.host = null;
        this.port = -1;
        this.path = null;
        this.query = Parameters.of();
        this.fragment = null;
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
    public ScxURIWritable path(String path) {
        this.path = path;
        return this;
    }

    @Override
    public ScxURIWritable query(Parameters<String, String> query) {
        this.query = Parameters.of(query);
        return this;
    }

    @Override
    public ScxURIWritable fragment(String fragment) {
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
    public String path() {
        return path;
    }

    @Override
    public ParametersWritable<String, String> query() {
        return query;
    }

    @Override
    public String fragment() {
        return fragment;
    }

    @Override
    public String toString() {
        return encode();
    }

}

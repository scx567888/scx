package cool.scx.http_server;

import cool.scx.common.util.MultiMap;

import java.util.Iterator;

public class ScxHttpHeadersImpl implements ScxHttpHeadersWritable {

    private final MultiMap<ScxHttpHeaderName, String> map;

    public ScxHttpHeadersImpl() {
        this.map = new MultiMap<>();
    }

    @Override
    public ScxHttpHeadersWritable set(ScxHttpHeader header) {
        map.setAll(header.name(), header.allValues());
        return this;
    }

    @Override
    public ScxHttpHeadersWritable add(ScxHttpHeader header) {
        map.putAll(header.name(), header.allValues());
        return this;
    }

    @Override
    public ScxHttpHeadersWritable set(ScxHttpHeaderName headerName, String headerValue) {
        map.set(headerName, headerValue);
        return this;
    }

    @Override
    public ScxHttpHeadersWritable add(ScxHttpHeaderName headerName, String headerValue) {
        map.put(headerName, headerValue);
        return this;
    }

    @Override
    public ScxHttpHeader get(ScxHttpHeaderName headerName) {
        var values = map.get(headerName);
        return new ScxHttpHeaderImpl(headerName, values);
    }

    @Override
    public Iterator<ScxHttpHeader> iterator() {
        return null;
    }

}

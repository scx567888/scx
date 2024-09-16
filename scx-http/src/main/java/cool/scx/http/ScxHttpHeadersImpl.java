package cool.scx.http;

import cool.scx.common.util.MultiMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

class ScxHttpHeadersImpl implements ScxHttpHeadersWritable {

    private final MultiMap<ScxHttpHeaderName, String> map = new MultiMap<>();

    @Override
    public ScxHttpHeadersWritable set(ScxHttpHeaderName headerName, String... headerValue) {
        this.map.setAll(headerName, List.of(headerValue));
        return this;
    }

    @Override
    public ScxHttpHeadersWritable set(String headerName, String... headerValue) {
        return set(ScxHttpHeaderName.of(headerName), headerValue);
    }

    @Override
    public ScxHttpHeadersWritable add(ScxHttpHeaderName headerName, String... headerValue) {
        this.map.putAll(headerName, List.of(headerValue));
        return this;
    }

    @Override
    public ScxHttpHeadersWritable add(String headerName, String... headerValue) {
        return add(ScxHttpHeaderName.of(headerName), headerValue);
    }

    @Override
    public ScxHttpHeadersWritable remove(ScxHttpHeaderName headerName) {
        map.removeAll(headerName);
        return this;
    }

    @Override
    public ScxHttpHeadersWritable remove(String headerName) {
        return remove(ScxHttpHeaderName.of(headerName));
    }

    @Override
    public String get(ScxHttpHeaderName headerName) {
        return map.getFirst(headerName);
    }

    @Override
    public String get(String headerName) {
        return get(ScxHttpHeaderName.of(headerName));
    }

    @Override
    public List<String> getAll(ScxHttpHeaderName headerName) {
        return map.get(headerName);
    }

    @Override
    public List<String> getAll(String headerName) {
        return getAll(ScxHttpHeaderName.of(headerName));
    }

    @Override
    public Iterator<Map.Entry<ScxHttpHeaderName, List<String>>> iterator() {
        return map.toMultiValueMap().entrySet().iterator();
    }

}

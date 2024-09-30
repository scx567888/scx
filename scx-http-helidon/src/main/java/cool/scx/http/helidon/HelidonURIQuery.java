package cool.scx.http.helidon;

import cool.scx.http.ParametersWritable;
import io.helidon.common.uri.UriQuery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HelidonURIQuery
 */
class HelidonURIQuery implements ParametersWritable<String, String> {

    private final UriQuery uriQuery;

    public HelidonURIQuery(UriQuery uriQuery) {
        this.uriQuery = uriQuery;
    }

    @Override
    public long size() {
        return uriQuery.size();
    }

    @Override
    public Set<String> names() {
        return uriQuery.names();
    }

    @Override
    public String get(String name) {
        return uriQuery.get(name);
    }

    @Override
    public List<String> getAll(String name) {
        return uriQuery.all(name);
    }

    @Override
    public Iterator<Map.Entry<String, List<String>>> iterator() {
        return uriQuery.toMap().entrySet().iterator();
    }

    @Override
    public Map<String, List<String>> toMap() {
        return uriQuery.toMap();
    }

    @Override
    public ParametersWritable<String, String> set(String name, String... value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParametersWritable<String, String> add(String name, String... value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParametersWritable<String, String> remove(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParametersWritable<String, String> clear() {
        throw new UnsupportedOperationException();
    }

}

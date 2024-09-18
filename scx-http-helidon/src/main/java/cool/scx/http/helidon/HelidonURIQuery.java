package cool.scx.http.helidon;

import cool.scx.http.ParametersWritable;
import cool.scx.http.uri.URIQueryWritable;
import io.helidon.common.uri.UriQuery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HelidonURIQuery
 */
class HelidonURIQuery implements URIQueryWritable {

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
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Map.Entry<String, List<String>> next() {
                return null;
            }
        };
    }

    @Override
    public Map<String, List<String>> toMap() {
        return uriQuery.toMap();
    }

    @Override
    public ParametersWritable set(String name, String... value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParametersWritable add(String name, String... value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParametersWritable remove(String name) {
        throw new UnsupportedOperationException();
    }

}

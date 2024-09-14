package cool.scx.http.helidon;

import cool.scx.http.uri.URIQuery;
import io.helidon.common.uri.UriQuery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

class HelidonURIQuery implements URIQuery {

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

}

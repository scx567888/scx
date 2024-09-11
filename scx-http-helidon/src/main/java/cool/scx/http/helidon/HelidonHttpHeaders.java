package cool.scx.http.helidon;

import cool.scx.http.ScxHttpHeaderName;
import cool.scx.http.ScxHttpHeaders;
import io.helidon.http.HeaderNames;
import io.helidon.http.Headers;

import java.util.*;

class HelidonHttpHeaders<T extends Headers> implements ScxHttpHeaders {

    protected final T headers;

    public HelidonHttpHeaders(T headers) {
        this.headers = headers;
    }

    @Override
    public String get(ScxHttpHeaderName headerName) {
        var all = getAll(headerName);
        return all.isEmpty() ? null : all.getFirst();
    }

    @Override
    public String get(String headerName) {
        var all = getAll(headerName);
        return all.isEmpty() ? null : all.getFirst();
    }

    @Override
    public List<String> getAll(ScxHttpHeaderName headerName) {
        return getAll(headerName.value());
    }

    @Override
    public List<String> getAll(String headerName) {
        try {
            return this.headers.get(HeaderNames.create(headerName)).allValues();
        } catch (NoSuchElementException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Iterator<Map.Entry<String, List<String>>> iterator() {
        var s = this.headers.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return s.hasNext();
            }

            @Override
            public Map.Entry<String, List<String>> next() {
                var next = s.next();
                return new AbstractMap.SimpleEntry<>(next.name(), next.allValues());
            }
        };
    }

}

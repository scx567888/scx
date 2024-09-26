package cool.scx.http.helidon;

import cool.scx.http.ScxHttpHeaderName;
import cool.scx.http.ScxHttpHeaders;
import io.helidon.http.HeaderNames;
import io.helidon.http.Headers;

import java.util.*;
import java.util.stream.Collectors;

public class HelidonHttpHeaders implements ScxHttpHeaders {

    private final Headers headers;

    public HelidonHttpHeaders(Headers headers) {
        this.headers = headers;
    }

    @Override
    public long size() {
        return headers.size();
    }

    @Override
    public Set<ScxHttpHeaderName> names() {
        return headers.stream().map(c -> ScxHttpHeaderName.of(c.name())).collect(Collectors.toSet());
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
    public Iterator<Map.Entry<ScxHttpHeaderName, List<String>>> iterator() {
        var s = this.headers.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return s.hasNext();
            }

            @Override
            public Map.Entry<ScxHttpHeaderName, List<String>> next() {
                var next = s.next();
                return new AbstractMap.SimpleEntry<>(ScxHttpHeaderName.of(next.name()), next.allValues());
            }
        };
    }

}

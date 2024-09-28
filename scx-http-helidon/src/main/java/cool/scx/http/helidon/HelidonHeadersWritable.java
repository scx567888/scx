package cool.scx.http.helidon;

import cool.scx.http.ParametersWritable;
import cool.scx.http.ScxHttpHeaderName;
import cool.scx.http.ScxHttpHeadersWritable;
import io.helidon.http.HeaderNames;
import io.helidon.http.ServerResponseHeaders;

import java.util.*;
import java.util.stream.Collectors;

public class HelidonHeadersWritable implements ScxHttpHeadersWritable {

    private final ServerResponseHeaders headers;

    public HelidonHeadersWritable(ServerResponseHeaders headers) {
        this.headers = headers;
    }

    @Override
    public ParametersWritable<ScxHttpHeaderName, String> set(ScxHttpHeaderName name, String... value) {
        headers.set(HeaderNames.create(name.value()), value);
        return this;
    }

    @Override
    public ParametersWritable<ScxHttpHeaderName, String> add(ScxHttpHeaderName name, String... value) {
        headers.add(HeaderNames.create(name.value()), value);
        return this;
    }

    @Override
    public ParametersWritable<ScxHttpHeaderName, String> remove(ScxHttpHeaderName name) {
        headers.remove(HeaderNames.create(name.value()));
        return this;
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
    public String get(ScxHttpHeaderName name) {
        return headers.value(HeaderNames.create(name.value())).orElse(null);
    }

    @Override
    public List<String> getAll(ScxHttpHeaderName name) {
        return headers.values(HeaderNames.create(name.value()));
    }

    @Override
    public Iterator<Map.Entry<ScxHttpHeaderName, List<String>>> iterator() {
        var iterator = headers.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Map.Entry<ScxHttpHeaderName, List<String>> next() {
                var next = iterator.next();
                return new AbstractMap.SimpleEntry<>(ScxHttpHeaderName.of(next.name()), next.allValues());
            }
        };
    }

}

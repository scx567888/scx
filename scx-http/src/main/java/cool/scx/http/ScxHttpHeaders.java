package cool.scx.http;

import java.util.List;

import static cool.scx.http.ScxHttpHeadersHelper.parseHeaders;

/**
 * ScxHttpHeaders
 */
public interface ScxHttpHeaders extends Parameters<ScxHttpHeaderName, String> {

    static ScxHttpHeadersWritable of() {
        return new ScxHttpHeadersImpl();
    }

    static ScxHttpHeadersWritable of(String headerStr) {
        return parseHeaders(headerStr);
    }

    default String get(String name) {
        return get(ScxHttpHeaderName.of(name));
    }

    default List<String> getAll(String name) {
        return getAll(ScxHttpHeaderName.of(name));
    }

    default boolean contains(String name) {
        return contains(ScxHttpHeaderName.of(name));
    }

}

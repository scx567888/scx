package cool.scx.http;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ScxHttpHeaders
 */
public interface ScxHttpHeaders extends Iterable<Map.Entry<ScxHttpHeaderName, List<String>>> {

    static ScxHttpHeadersWritable of() {
        return new ScxHttpHeadersImpl();
    }

    long size();

    Set<ScxHttpHeaderName> names();

    String get(ScxHttpHeaderName headerName);

    String get(String headerName);

    List<String> getAll(ScxHttpHeaderName headerName);

    List<String> getAll(String headerName);

    default boolean contains(ScxHttpHeaderName httpFieldName) {
        return get(httpFieldName) != null;
    }

    default boolean contains(String httpFieldName) {
        return get(httpFieldName) != null;
    }

    default boolean isEmpty() {
        return this.size() == 0;
    }

}

package cool.scx.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parameters
 */
public interface Parameters extends Iterable<Map.Entry<String, List<String>>> {

    static ParametersWritable of() {
        return new ParametersImpl();
    }

    long size();

    Set<String> names();

    String get(String name);

    List<String> getAll(String name);

    default boolean isEmpty() {
        return this.size() == 0;
    }

    default Map<String, List<String>> toMap() {
        var map = new HashMap<String, List<String>>();
        for (var stringListEntry : this) {
            map.put(stringListEntry.getKey(), stringListEntry.getValue());
        }
        return map;
    }

}

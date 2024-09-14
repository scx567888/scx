package cool.scx.http;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Parameters extends Iterable<Map.Entry<String, List<String>>> {

    static ParametersWritable of() {
        return new ParametersImpl();
    }

    default boolean isEmpty() {
        return this.size() == 0;
    }

    long size();

    Set<String> names();

    String get(String name);

    List<String> getAll(String name);

}

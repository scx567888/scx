package cool.scx.http;

import cool.scx.common.util.MultiMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ParametersImpl
 */
class ParametersImpl implements ParametersWritable {

    private final MultiMap<String, String> map = new MultiMap<>();

    @Override
    public ParametersWritable set(String name, String... value) {
        map.setAll(name, List.of(value));
        return this;
    }

    @Override
    public ParametersWritable add(String name, String... value) {
        map.putAll(name, List.of(value));
        return this;
    }

    @Override
    public ParametersWritable remove(String name) {
        map.removeAll(name);
        return this;
    }

    @Override
    public long size() {
        return map.size();
    }

    @Override
    public Set<String> names() {
        return map.keySet();
    }

    @Override
    public String get(String name) {
        return map.getFirst(name);
    }

    @Override
    public List<String> getAll(String name) {
        return map.get(name);
    }

    @Override
    public Iterator<Map.Entry<String, List<String>>> iterator() {
        return map.toMultiValueMap().entrySet().iterator();
    }

}

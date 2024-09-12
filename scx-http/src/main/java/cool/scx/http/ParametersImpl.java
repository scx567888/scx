package cool.scx.http;

import cool.scx.common.util.MultiMap;

import java.util.List;

public class ParametersImpl implements ParametersWritable {

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
    public String get(String name) {
        return map.getFirst(name);
    }

    @Override
    public List<String> getAll(String name) {
        return map.get(name);
    }

}

package cool.scx.http_server;

import cool.scx.common.util.MultiMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScxHttpPathQueryImpl implements ScxHttpPathQueryWritable {

    private final MultiMap<String, String> map;

    public ScxHttpPathQueryImpl() {
        this.map = new MultiMap<>();
    }

    @Override
    public ScxHttpPathQueryImpl set(String name, String value) {
        map.set(name, value);
        return this;
    }

    @Override
    public ScxHttpPathQueryImpl add(String name, String value) {
        map.put(name, value);
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

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return null;
    }
    
}

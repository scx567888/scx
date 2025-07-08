package cool.scx.object.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectNode implements ContainerNode, Iterable<Map.Entry<String, Node>> {

    private final Map<String, Node> _children;

    public ObjectNode() {
        this._children = new LinkedHashMap<>();
    }

    public void put(String name, Node node) {
        this._children.put(name, node);
    }

    public Node get(String name) {
        return this._children.get(name);
    }

    public Node remove(String name) {
        return this._children.remove(name);
    }

    @Override
    public Iterator<Map.Entry<String, Node>> iterator() {
        return _children.entrySet().iterator();
    }

}

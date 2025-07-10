package cool.scx.object.node;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ObjectNode implements Node, Iterable<Map.Entry<String, Node>> {

    private final Map<String, Node> _children;

    public ObjectNode() {
        this._children = new LinkedHashMap<>();
    }

    public Node put(String name, Node node) {
        return this._children.put(name, node);
    }

    public Node putIfAbsent(String name, Node node) {
        return this._children.putIfAbsent(name, node);
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

    @Override
    public String toString() {
        return _children.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ObjectNode objectNode) {
            return _children.equals(objectNode._children);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return _children.hashCode();
    }

}

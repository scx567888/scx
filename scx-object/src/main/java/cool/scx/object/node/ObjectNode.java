package cool.scx.object.node;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/// ObjectNode
///
/// @author scx567888
/// @version 0.0.1
public final class ObjectNode implements Node, Iterable<Map.Entry<String, Node>> {

    private final Map<String, Node> _children;

    public ObjectNode() {
        this._children = new LinkedHashMap<>();
    }

    public ObjectNode(int initialCapacity) {
        this._children = new LinkedHashMap<>(initialCapacity);
    }

    public Node put(String name, Node value) {
        if (name == null) {
            throw new NullPointerException("ObjectNode cannot put null name");
        }
        if (value == null) {
            throw new NullPointerException("ObjectNode cannot put null value");
        }
        return this._children.put(name, value);
    }

    public Node get(String name) {
        return this._children.get(name);
    }

    public Node remove(String name) {
        return this._children.remove(name);
    }

    public int size() {
        return _children.size();
    }

    public boolean isEmpty() {
        return _children.isEmpty();
    }

    @Override
    public Iterator<Map.Entry<String, Node>> iterator() {
        return _children.entrySet().iterator();
    }

    @Override
    public ObjectNode deepCopy() {
        // 这里假设 ObjectNode 不存在自引用
        var objectNode = new ObjectNode();
        for (var entry : this) {
            objectNode.put(entry.getKey(), entry.getValue().deepCopy());
        }
        return objectNode;
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

    @Override
    public String toString() {
        return _children.toString();
    }

}

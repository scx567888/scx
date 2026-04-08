package cool.scx.object.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/// ArrayNode
///
/// @author scx567888
/// @version 0.0.1
public final class ArrayNode implements Node, Iterable<Node> {

    private final List<Node> _children;

    public ArrayNode() {
        this._children = new ArrayList<>();
    }

    public ArrayNode(int initialCapacity) {
        this._children = new ArrayList<>(initialCapacity);
    }

    public void add(Node node) {
        if (node == null) {
            throw new NullPointerException("ArrayNode cannot add null");
        }
        this._children.add(node);
    }

    public void add(int index, Node node) {
        if (node == null) {
            throw new NullPointerException("ArrayNode cannot add null");
        }
        this._children.add(index, node);
    }

    public void set(int index, Node node) {
        if (node == null) {
            throw new NullPointerException("ArrayNode cannot set null");
        }
        this._children.set(index, node);
    }

    public Node get(int index) {
        return this._children.get(index);
    }

    public Node remove(int index) {
        return this._children.remove(index);
    }

    public int size() {
        return this._children.size();
    }

    public boolean isEmpty() {
        return this._children.isEmpty();
    }

    @Override
    public Iterator<Node> iterator() {
        return _children.iterator();
    }

    @Override
    public ArrayNode deepCopy() {
        // 这里假设 ArrayNode 不存在自引用
        var arrayNode = new ArrayNode(size());
        for (var node : this) {
            arrayNode.add(node.deepCopy());
        }
        return arrayNode;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ArrayNode arrayNode) {
            return _children.equals(arrayNode._children);
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

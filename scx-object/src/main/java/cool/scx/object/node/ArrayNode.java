package cool.scx.object.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ArrayNode implements ContainerNode, Iterable<Node> {

    private final List<Node> _children;

    public ArrayNode() {
        this._children = new ArrayList<>();
    }

    public void add(Node node) {
        this._children.add(node);
    }

    public void add(int index, Node node) {
        this._children.add(index, node);
    }

    public void set(int index, Node node) {
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

    @Override
    public Iterator<Node> iterator() {
        return _children.iterator();
    }

    @Override
    public String toString() {
        return _children.toString();
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

}

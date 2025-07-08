package cool.scx.object.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayNode implements ContainerNode, Iterable<Node> {

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

    @Override
    public Iterator<Node> iterator() {
        return _children.iterator();
    }

}

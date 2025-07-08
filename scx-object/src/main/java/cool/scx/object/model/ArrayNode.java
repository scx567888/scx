package cool.scx.object.model;

import java.util.ArrayList;
import java.util.List;

public class ArrayNode implements ContainerNode {

    private final List<Node> _children;

    public ArrayNode() {
        this._children = new ArrayList<>();
    }

    public void add(Node node) {
        this._children.add(node);
    }

}

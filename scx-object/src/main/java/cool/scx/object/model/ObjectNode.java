package cool.scx.object.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectNode implements ContainerNode {

    private final Map<String, Node> _children;

    public ObjectNode() {
        this._children = new LinkedHashMap<>();
    }

    public void put(String name, Node node) {
        this._children.put(name, node);
    }

}

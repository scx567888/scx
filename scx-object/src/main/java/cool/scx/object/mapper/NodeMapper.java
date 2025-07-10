package cool.scx.object.mapper;

import cool.scx.object.node.Node;

public interface NodeMapper<T> {
    
    Node toNode(T value);
    
    T fromNode(Node node);
    
}

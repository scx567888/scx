package cool.scx.object.serializer;

import cool.scx.object.node.Node;

public interface NodeSerializer {

    String serializeAsString(Node node) throws NodeSerializeException;

}

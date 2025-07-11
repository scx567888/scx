package cool.scx.object.merger;

import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;

public class NodeMerger {
    
    public void merge(Node target, Node source) {
        if (target instanceof ObjectNode targetObj && source instanceof ObjectNode sourceObj) {
            mergeObjectNode(targetObj, sourceObj);
        } else if (target instanceof ArrayNode targetArr && source instanceof ArrayNode sourceArr) {
            mergeArrayNode(targetArr, sourceArr);
        } else {
            throw new IllegalArgumentException("Cannot merge nodes of type: " + target.getClass() + " and " + source.getClass());
        }
    }

    private void mergeObjectNode(ObjectNode target, ObjectNode source) {
        for (var entry : source) {
            var key = entry.getKey();
            var sourceNode = entry.getValue();
            var targetNode = target.get(key);
            if (targetNode != null) {
                // 两个 key 都有，递归合并
                merge(targetNode, sourceNode);
            } else {
                // 只在 target 没有该 key 时，直接 put
                target.put(key, sourceNode);
            }
        }
    }

    private void mergeArrayNode(ArrayNode target, ArrayNode source) {
        for (var node : source) {
            target.add(node);
        }
    }
    
}

package cool.scx.object.merger;

import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;

/// 将 source 合并到 target 中(深合并).
///
/// 合并规则如下：
///
///   - 当 target 与 source 都是 ObjectNode 时, 递归地逐个 key 合并.
///   - 当 target 与 source 都是 ArrayNode 时, source 节点会被追加到 target 的末尾.
///   - 当类型不一致, 或者某个 key 下 target 是值节点, source 也是值节点时, 直接用 source 节点覆盖 target 节点.
///   - 如果 target 和 source 类型不同(且不是 ObjectNode 或 ArrayNode), 则抛出异常.
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
            if (targetNode instanceof ObjectNode && sourceNode instanceof ObjectNode) {
                merge(targetNode, sourceNode);
            } else if (targetNode instanceof ArrayNode && sourceNode instanceof ArrayNode) {
                merge(targetNode, sourceNode);
            } else {
                // 不同类型或者值类型, 直接覆盖
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

package cool.scx.object.node;

/// Node
///
/// @author scx567888
/// @version 0.0.1
public sealed interface Node permits ValueNode, ArrayNode, ObjectNode, NullNode {

    Node deepCopy();

    /// 判断是否为 NullNode.
    /// 仅提供此方法, 因为其他 isXxx 方法即使存在, 也仍需强转才能使用, 意义不大.
    default boolean isNull() {
        return this == NullNode.NULL;
    }

}

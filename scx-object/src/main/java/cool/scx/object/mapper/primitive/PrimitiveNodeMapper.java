package cool.scx.object.mapper.primitive;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.node.Node;

/// 处理原始类型 因为原始类型 不能为空 (必须有一个值) 所以使用此类支持 默认值处理
///
/// @param <T>
/// @author scx567888
/// @version 0.0.1
abstract class PrimitiveNodeMapper<T> implements NodeMapper<T> {

    final boolean isPrimitive;

    final T defaultValue;

    PrimitiveNodeMapper(boolean isPrimitive, T defaultValue) {
        this.isPrimitive = isPrimitive;
        // 处理基类以外全使用 null
        this.defaultValue = isPrimitive ? defaultValue : null;
    }

    @Override
    public final T fromNode(Node node) {
        T object = fromNode0(node);
        // 基类不允许为空 所以我们在检测到基类为空时 返回默认值
        if (isPrimitive && object == null) {
            return defaultValue;
        }
        return object;
    }

    public abstract T fromNode0(Node node);

}

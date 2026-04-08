package cool.scx.object.mapping;

import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;

/// 默认映射上下文
///
/// 一般来说 这里应该支持递归引用检测,
/// 但经过实际测试有些耗费性能, 这里暂时只提供 最大嵌套层数检测
///
/// @author scx567888
/// @version 0.0.1
public final class ToNodeContextImpl implements ToNodeContext {

    // NodeMapper 选择器
    private final NodeMapperSelector selector;
    // 配置选项
    private final ToNodeOptionsImpl options;
    // 当前深度
    private int nestingDepth;

    public ToNodeContextImpl(NodeMapperSelector selector, ToNodeOptionsImpl options) {
        this.selector = selector;
        this.options = options;
        this.nestingDepth = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Node toNode(Object value, Object pathSegment) throws NodeMappingException {
        if (value == null) {
            return NullNode.NULL;
        }
        //判断嵌套深度
        if (nestingDepth > options.maxNestingDepth()) {
            throw new NodeMappingException("嵌套深度超过限制: 最大 " + options.maxNestingDepth());
        }
        nestingDepth = nestingDepth + 1;
        try {
            var nodeMapper = (NodeMapper<Object>) selector.findNodeMapper(value.getClass());
            return nodeMapper.toNode(value, this);
        } finally {
            //回退嵌套深度
            nestingDepth = nestingDepth - 1;
        }
    }

    @Override
    public ToNodeOptions options() {
        return options;
    }

}

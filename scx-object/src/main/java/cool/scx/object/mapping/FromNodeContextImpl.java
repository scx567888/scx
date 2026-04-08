package cool.scx.object.mapping;

import cool.scx.object.node.Node;
import cool.scx.reflect.TypeInfo;

/// 默认映射上下文
///
/// 一般来说 这里应该支持递归引用检测 (是的 Node 也是可能递归引用的),
/// 但经过实际测试有些耗费性能, 这里暂时只提供 最大嵌套层数检测
///
/// @author scx567888
/// @version 0.0.1
public final class FromNodeContextImpl implements FromNodeContext {

    // NodeMapper 选择器
    private final NodeMapperSelector selector;
    // 配置选项
    private final FromNodeOptionsImpl options;
    // 当前深度
    private int nestingDepth;

    public FromNodeContextImpl(NodeMapperSelector selector, FromNodeOptionsImpl options) {
        this.selector = selector;
        this.options = options;
        this.nestingDepth = 0;
    }

    @SuppressWarnings("unchecked")
    public <T> T fromNode(Node node, TypeInfo type) throws NodeMappingException {
        if (nestingDepth > options.maxNestingDepth()) {
            throw new NodeMappingException("嵌套深度超过限制: 最大 " + options.maxNestingDepth());
        }
        nestingDepth = nestingDepth + 1;
        try {
            var nodeMapper = selector.findNodeMapper(type);
            return (T) nodeMapper.fromNode(node, this);
        } finally {
            //回退嵌套深度
            nestingDepth = nestingDepth - 1;
        }
    }

    @Override
    public FromNodeOptions options() {
        return options;
    }

}

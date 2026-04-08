package cool.scx.object.mapping;

import cool.scx.reflect.TypeInfo;

public interface NodeMapperFactory {

    /// 无法处理返回 null
    NodeMapper<?> createNodeMapper(TypeInfo typeInfo, NodeMapperSelector selector) throws NodeMappingException;

}

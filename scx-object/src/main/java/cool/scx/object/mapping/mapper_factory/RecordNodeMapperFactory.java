package cool.scx.object.mapping.mapper_factory;

import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMapperFactory;
import cool.scx.object.mapping.NodeMapperSelector;
import cool.scx.object.mapping.mapper.record.RecordNodeMapper;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.ClassKind;
import cool.scx.reflect.TypeInfo;

public class RecordNodeMapperFactory implements NodeMapperFactory {

    @Override
    public NodeMapper<?> createNodeMapper(TypeInfo typeInfo, NodeMapperSelector selector) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (classInfo.classKind() == ClassKind.RECORD) {
                return new RecordNodeMapper(classInfo);
            }
        }
        return null;
    }

}

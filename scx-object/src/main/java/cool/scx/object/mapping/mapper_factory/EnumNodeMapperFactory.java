package cool.scx.object.mapping.mapper_factory;

import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMapperFactory;
import cool.scx.object.mapping.NodeMapperSelector;
import cool.scx.object.mapping.mapper.EnumNodeMapper;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.ClassKind;
import cool.scx.reflect.TypeInfo;

public class EnumNodeMapperFactory implements NodeMapperFactory {

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> EnumNodeMapper<E> createEnumNodeMapper(ClassInfo classInfo) {
        return new EnumNodeMapper<>((Class<E>) classInfo.enumClass().rawClass());
    }

    @Override
    public NodeMapper<?> createNodeMapper(TypeInfo typeInfo, NodeMapperSelector selector) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (classInfo.classKind() == ClassKind.ENUM) {
                return createEnumNodeMapper(classInfo);
            }
        }
        return null;
    }

}

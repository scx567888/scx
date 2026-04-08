package cool.scx.object.mapping.mapper_factory;

import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMapperFactory;
import cool.scx.object.mapping.NodeMapperSelector;
import cool.scx.object.mapping.mapper.bean.BeanNodeMapper;
import cool.scx.reflect.*;

import java.util.Arrays;

public class BeanNodeMapperFactory implements NodeMapperFactory {

    private static FieldInfo[] filterReadableFields(ClassInfo classInfo) {
        // 因为 ObjectNodeMapper 每种类型的对象只会创建一次, 所以这里 使用 Stream 并没有什么性能问题
        // 注意我们这里需要连父级的字段也带上 
        return Arrays.stream(classInfo.allFields())
                .filter(c -> !c.isStatic() && c.accessModifier() == AccessModifier.PUBLIC)
                .peek(c -> c.setAccessible(true))// 处理一些类本身 就不是 public 的情况, 比如内部类
                .toArray(FieldInfo[]::new);
    }

    private static FieldInfo[] filterWritableFields(FieldInfo[] readableFields) {
        // 因为 ObjectNodeMapper 每种类型的对象只会创建一次, 所以这里 使用 Stream 并没有什么性能问题
        return Arrays.stream(readableFields).filter(c -> !c.isFinal()).toArray(FieldInfo[]::new);
    }

    @Override
    public NodeMapper<?> createNodeMapper(TypeInfo typeInfo, NodeMapperSelector selector) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (classInfo.classKind() == ClassKind.CLASS) {
                if (!classInfo.isAbstract()) {
                    // 这里我们只是获取一下这个 构造器, 并不进行 是否存在或是否可访问的校验, 
                    // 因为有时候 ObjectNodeMapper 只会被用作 toNode 根本用不上 defaultConstructor, 所以这里延后校验以提供更强的容错性.
                    var defaultConstructor = classInfo.defaultConstructor();
                    // 可读的字段, 这里只要 public 的实例字段
                    var readableFields = filterReadableFields(classInfo);
                    // 可写的字段, 相较于可读 我们过滤掉 final 
                    var writableFields = filterWritableFields(readableFields);
                    return new BeanNodeMapper(classInfo, defaultConstructor, readableFields, writableFields);
                }
            }
        }
        return null;
    }

}

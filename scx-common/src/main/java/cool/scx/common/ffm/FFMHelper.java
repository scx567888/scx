package cool.scx.common.ffm;

import cool.scx.common.ffm.type.*;
import cool.scx.common.ffm.type.wrapper.*;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.*;

public final class FFMHelper {

    public static MemoryLayout getMemoryLayout(Class<?> type) {
        //1, 先处理可以直接映射的基本类型
        if (type == Byte.class || type == byte.class) {
            return JAVA_BYTE;
        }
        if (type == Boolean.class || type == boolean.class) {
            return JAVA_BOOLEAN;
        }
        if (type == Character.class || type == char.class) {
            return JAVA_CHAR;
        }
        if (type == Short.class || type == short.class) {
            return JAVA_SHORT;
        }
        if (type == Integer.class || type == int.class) {
            return JAVA_INT;
        }
        if (type == Long.class || type == long.class) {
            return JAVA_LONG;
        }
        if (type == Float.class || type == float.class) {
            return JAVA_FLOAT;
        }
        if (type == Double.class || type == double.class) {
            return JAVA_DOUBLE;
        }
        if (type == MemorySegment.class) {
            return ADDRESS;
        }
        //2, 处理基本类型的简单包装类型
        if (ByteWrapper.class.isAssignableFrom(type)) {
            return JAVA_BYTE;
        }
        if (BooleanWrapper.class.isAssignableFrom(type)) {
            return JAVA_BOOLEAN;
        }
        if (CharWrapper.class.isAssignableFrom(type)) {
            return JAVA_CHAR;
        }
        if (ShortWrapper.class.isAssignableFrom(type)) {
            return JAVA_SHORT;
        }
        if (IntWrapper.class.isAssignableFrom(type)) {
            return JAVA_INT;
        }
        if (LongWrapper.class.isAssignableFrom(type)) {
            return JAVA_LONG;
        }
        if (FloatWrapper.class.isAssignableFrom(type)) {
            return JAVA_FLOAT;
        }
        if (DoubleWrapper.class.isAssignableFrom(type)) {
            return JAVA_DOUBLE;
        }
        if (AddressWrapper.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        //3, 处理字符串
        if (String.class == type) {
            return ADDRESS;
        }
        //4, 处理引用类型
        if (Ref.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        //5, 处理 结构体类型 这里我们使用 ADDRESS 而不使用 MemoryLayout.structLayout(), 因为需要在运行时才知道具体结构
        if (Struct.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        //6, 处理 Callback 类型
        if (Callback.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        //7, 处理基本类型的数组类型 这里我们使用 ADDRESS 而不使用 MemoryLayout.sequenceLayout() , 因为我们不知道数组长度
        if (type.isArray() && type.getComponentType().isPrimitive()) {
            return ADDRESS;
        }
        throw new IllegalArgumentException("不支持的参数类型 !!! " + type);
    }

    public static MemoryLayout[] getMemoryLayouts(Class<?>[] types) {
        var memoryLayouts = new MemoryLayout[types.length];
        for (var i = 0; i < types.length; i = i + 1) {
            memoryLayouts[i] = getMemoryLayout(types[i]);
        }
        return memoryLayouts;
    }

    public static Parameter convertToParameter(Object o) throws NoSuchMethodException, IllegalAccessException {
        return switch (o) {
            case null -> new RawParameter(MemorySegment.NULL);
            case Long _, Integer _, MemorySegment _ -> new RawParameter(o);
            case String s -> new StringParameter(s);
            case char[] c -> new CharArrayRef(c);
            case Callback c -> new CallbackParameter(c);
            case Struct c -> new StructRef(c);
            case Parameter r -> r;
            default -> throw new RuntimeException("无法转换的类型 !!! " + o.getClass());
        };
    }

    public static Parameter[] convertToParameters(Object[] objs) throws NoSuchMethodException, IllegalAccessException {
        var result = new Parameter[objs.length];
        for (var i = 0; i < objs.length; i = i + 1) {
            result[i] = convertToParameter(objs[i]);
        }
        return result;
    }

}

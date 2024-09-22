package cool.scx.common.ffm;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.foreign.ValueLayout.*;

public final class FFMHelper {

    public static MethodHandle findMethodHandle(SymbolLookup lookup, Method method) {
        //1, 根据方法名查找对应的方法
        var fun = lookup.find(method.getName()).orElseThrow();
        //2, 创建方法的描述 , 包括 返回值类型 参数类型列表
        var returnLayout = getMemoryLayout(method.getReturnType());
        var paramLayouts = getMemoryLayouts(method.getParameterTypes());
        var functionDescriptor = FunctionDescriptor.of(returnLayout, paramLayouts);
        //3, 根据方法和描述 获取方法句柄
        return nativeLinker().downcallHandle(fun, functionDescriptor);
    }

    public static MemoryLayout getMemoryLayout(Class<?> type) {
        if (type == Byte.class || type == byte.class) {
            return JAVA_BYTE;
        } else if (type == Boolean.class || type == boolean.class) {
            return JAVA_BOOLEAN;
        } else if (type == Character.class || type == char.class) {
            return JAVA_CHAR;
        } else if (type == Short.class || type == short.class) {
            return JAVA_SHORT;
        } else if (type == Integer.class || type == int.class) {
            return JAVA_INT;
        } else if (type == Long.class || type == long.class) {
            return JAVA_LONG;
        } else if (type == Float.class || type == float.class) {
            return JAVA_FLOAT;
        } else if (type == Double.class || type == double.class) {
            return JAVA_DOUBLE;
        } else if (type == String.class ||
                   type == MemorySegment.class ||
                   type.isArray() ||
                   Ref.class.isAssignableFrom(type) ||
                   Callback.class.isAssignableFrom(type) ||
                   Struct.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        throw new IllegalArgumentException("不支持的参数类型 !!! " + type);
    }

    public static MemoryLayout[] getMemoryLayouts(Class<?>[] types) {
        var array = new MemoryLayout[types.length];
        for (var i = 0; i < types.length; i = i + 1) {
            array[i] = getMemoryLayout(types[i]);
        }
        return array;
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

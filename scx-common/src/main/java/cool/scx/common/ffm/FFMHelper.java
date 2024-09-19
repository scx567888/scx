package cool.scx.common.ffm;

import cool.scx.common.ffm.type.*;
import cool.scx.common.reflect.ReflectFactory;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.foreign.ValueLayout.*;
import static java.lang.invoke.MethodHandles.lookup;

public final class FFMHelper {

    /**
     * 从指定的 SymbolLookup 中寻找 MethodHandle
     *
     * @param symbolLookup       symbolLookup
     * @param name               方法名称
     * @param functionDescriptor 方法描述
     * @return MethodHandle
     */
    public static MethodHandle findMethodHandle(SymbolLookup symbolLookup, String name, FunctionDescriptor functionDescriptor) {
        return nativeLinker().downcallHandle(symbolLookup.find(name).orElseThrow(), functionDescriptor);
    }

    /**
     * 根据 Method 获取 对应的方法描述
     *
     * @param method method
     * @return 对应的方法描述
     */
    public static FunctionDescriptor getFunctionDescriptor(Method method) {
        return FunctionDescriptor.of(getMemoryLayout(method.getReturnType()), getMemoryLayouts(method.getParameterTypes()));
    }

    /**
     * 根据 Class 获取对应的 MemoryLayout 类型
     *
     * @param type 类型
     * @return 对应的 MemoryLayout 类型
     */
    public static MemoryLayout getMemoryLayout(Class<?> type) {
        if (type == Long.class || type == long.class) {
            return JAVA_LONG;
        } else if (type == Integer.class || type == int.class) {
            return JAVA_INT;
        } else if (type == Boolean.class || type == boolean.class) {
            return JAVA_BOOLEAN;
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

    /**
     * 根据 Class 获取对应的 MemoryLayout 类型
     *
     * @param types 类型
     * @return 对应的 MemoryLayout 类型
     */
    public static MemoryLayout[] getMemoryLayouts(Class<?>[] types) {
        var array = new MemoryLayout[types.length];
        for (var i = 0; i < types.length; i = i + 1) {
            array[i] = getMemoryLayout(types[i]);
        }
        return array;
    }

    //todo 需要内部改变的数据 如 数组 等等如何处理
    public static Object convertParameter(Arena arena, Object o) throws NoSuchMethodException, IllegalAccessException {
        return switch (o) {
            case null -> MemorySegment.NULL;
            case Long _, MemorySegment _, Ref _, Integer _ -> o;
            case String s -> arena.allocateFrom(s);
            case char[] c -> new CharArrayRef(c);
            case Callback c -> convertCallback(arena, c);
            case Struct c -> new StructRef(c);
            default -> throw new RuntimeException("无法转换的类型 !!! " + o.getClass());
        };
    }

    public static MemorySegment convertCallback(Arena arena, Callback c) throws NoSuchMethodException, IllegalAccessException {
        //todo 此处待处理
        var cc = c.getClass().getInterfaces()[0];
        var s = ReflectFactory.getClassInfo(cc);
        var methodInfo = Arrays.stream(s.methods()).filter(method -> c.callbackMethodName().equals(method.name())).findFirst().orElseThrow(() -> new NoSuchMethodException("callback"));
        var _handle = lookup().unreflect(methodInfo.method()).bindTo(c);
        var r = getMemoryLayout(methodInfo.method().getReturnType());
        var p = getMemoryLayouts(methodInfo.method().getParameterTypes());
        return nativeLinker().upcallStub(_handle, FunctionDescriptor.of(r, p), arena);
    }

    public static Object[] convertParameters(Arena arena, Object[] o) throws NoSuchMethodException, IllegalAccessException {
        var array = new Object[o.length];
        for (var i = 0; i < o.length; i = i + 1) {
            array[i] = convertParameter(arena, o[i]);
        }
        return array;
    }


    public static Object[] initRefParameters(Arena arena, Object[] parameters) {
        var array = new Object[parameters.length];
        for (var i = 0; i < parameters.length; i = i + 1) {
            array[i] = initRef(arena, parameters[i]);
        }
        return array;
    }

    /**
     * 如果类型属于引用 则调用 init 方法
     *
     * @param arena     a
     * @param parameter a
     * @return a
     */
    private static Object initRef(Arena arena, Object parameter) {
        if (parameter instanceof Ref r) {
            return r.init(arena);
        }
        return parameter;
    }

    /**
     * 如果类型属于引用 则调用 end 方法
     *
     * @param parameters p
     */
    public static void refreshRefs(Object[] parameters) {
        for (var parameter : parameters) {
            if (parameter instanceof Ref r) {
                r.refresh();
            }
        }
    }

}

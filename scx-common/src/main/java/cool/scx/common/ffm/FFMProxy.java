package cool.scx.common.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.common.ffm.FFMHelper.*;
import static java.lang.foreign.Arena.global;
import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.foreign.SymbolLookup.libraryLookup;

public final class FFMProxy implements InvocationHandler {

    private final Map<Method, MethodHandle> cache;
    private final Class<?> clazz;
    private final SymbolLookup lookup;

    private FFMProxy(String name, Class<?> clazz) {
        this.lookup = name != null ? libraryLookup(name, global()) : nativeLinker().defaultLookup();
        this.clazz = clazz;
        this.cache = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> T ffmProxy(String name, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxy(name, clazz));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        var methodHandle = this.cache.computeIfAbsent(method, this::createMethodHandle);
        try (var arena = Arena.ofConfined()) {
            //1, 将参数进行转换
            var parameters = convertParameters(arena, args);
            //2, 将引用类型的参数转换为 methodHandle 可使用的参数
            var methodHandleParameters = initRefParameters(arena, parameters);
            //3, 执行方法
            var result = methodHandle.invokeWithArguments(methodHandleParameters);
            //4, 处理引用类型参数
            refreshRefs(parameters);
            //5, 返回结果
            return result;
        }
    }

    public MethodHandle createMethodHandle(Method method) {
        return findMethodHandle(this.lookup, method.getName(), getFunctionDescriptor(method));
    }

}

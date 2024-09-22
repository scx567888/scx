package cool.scx.common.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.common.ffm.FFMHelper.convertToParameters;
import static cool.scx.common.ffm.FFMHelper.findMethodHandle;
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
        // Object 的方法这里直接跳过, 我们只处理接口上的方法
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        var methodHandle = this.cache.computeIfAbsent(method, (m) -> findMethodHandle(this.lookup, m));

        try (var arena = Arena.ofConfined()) {

            //1, 将参数全部转换为 parameters 对象
            var parameters = convertToParameters(args);

            //2, 将 parameters 转换为 nativeParameters
            var nativeParameters = new Object[parameters.length];
            for (var i = 0; i < parameters.length; i = i + 1) {
                nativeParameters[i] = parameters[i].toNativeParameter(arena);
            }

            //3, 执行方法
            var result = methodHandle.invokeWithArguments(nativeParameters);

            //4, 针对 ref 进行特殊处理
            for (var parameter : parameters) {
                if (parameter instanceof Ref ref) {
                    ref.readFromMemorySegment();
                }
            }

            //5, 返回结果
            return result;

        }

    }

}

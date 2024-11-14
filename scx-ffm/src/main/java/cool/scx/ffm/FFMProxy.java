package cool.scx.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.ffm.FFMHelper.*;
import static java.lang.foreign.Arena.global;
import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.foreign.SymbolLookup.libraryLookup;

public final class FFMProxy implements InvocationHandler {

    private final SymbolLookup lookup;
    private final Map<Method, MethodHandle> cache;

    private FFMProxy() {
        this.lookup = nativeLinker().defaultLookup();
        this.cache = new HashMap<>();
    }

    private FFMProxy(String name) {
        this.lookup = libraryLookup(name, global());
        this.cache = new HashMap<>();
    }

    private FFMProxy(Path path) {
        this.lookup = libraryLookup(path, global());
        this.cache = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> T ffmProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxy());
    }

    @SuppressWarnings("unchecked")
    public static <T> T ffmProxy(String name, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxy(name));
    }

    @SuppressWarnings("unchecked")
    public static <T> T ffmProxy(Path path, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxy(path));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // Object 的方法这里直接跳过, 我们只处理接口上的方法
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        var methodHandle = this.cache.computeIfAbsent(method, this::findMethodHandle);

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
                parameter.beforeCloseArena();
            }

            //5, 返回结果
            return result;

        }

    }

    private MethodHandle findMethodHandle(Method method) {
        //1, 根据方法名查找对应的方法
        var fun = lookup.find(method.getName()).orElse(null);
        if (fun == null) {
            throw new IllegalArgumentException("未找到对应外部方法 : " + method.getName());
        }
        //2, 创建方法的描述, 包括 返回值类型 参数类型列表
        var returnLayout = getMemoryLayout(method.getReturnType());
        var paramLayouts = getMemoryLayouts(method.getParameterTypes());
        var functionDescriptor = FunctionDescriptor.of(returnLayout, paramLayouts);
        //3, 根据方法和描述, 获取可以调用本机方法的方法句柄
        return nativeLinker().downcallHandle(fun, functionDescriptor);
    }

}

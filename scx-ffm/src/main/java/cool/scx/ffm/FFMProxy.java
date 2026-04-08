package cool.scx.ffm;

import cool.scx.ffm.mapper.Mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.ffm.FFMHelper.*;
import static java.lang.foreign.Linker.nativeLinker;

/// FFMProxy
///
/// @author scx567888
/// @version 0.0.1
public final class FFMProxy implements InvocationHandler {

    private final SymbolLookup lookup;
    private final Map<Method, MethodHandle> cache;

    FFMProxy(SymbolLookup lookup) {
        this.lookup = lookup;
        this.cache = new HashMap<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // Object 的方法这里直接跳过, 我们只处理接口上的方法
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        var methodHandle = this.cache.computeIfAbsent(method, this::findMethodHandle);

        try (var arena = Arena.ofConfined()) {

            //1, 将参数全部转换为 基本类型 | MemorySegment | Mapper
            var parameters = convertToParameters(args);

            //2, 将 parameters 转换为 nativeParameters 基本类型 | MemorySegment
            var nativeParameters = new Object[args.length];
            for (var i = 0; i < parameters.length; i = i + 1) {
                var parameter = parameters[i];
                if (parameter instanceof Mapper mapper) {
                    nativeParameters[i] = mapper.toMemorySegment(arena);
                } else {
                    //能够直接使用的 保留原始值
                    nativeParameters[i] = parameter;
                }
            }

            //3, 执行方法
            var result = methodHandle.invokeWithArguments(nativeParameters);

            //4, Mapper 类型的参数 数据回写
            for (int i = 0; i < parameters.length; i = i + 1) {
                var parameter = parameters[i];
                var nativeParameter = nativeParameters[i];
                if (parameter instanceof Mapper mapper && nativeParameter instanceof MemorySegment memorySegment) {
                    mapper.fromMemorySegment(memorySegment);
                }
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

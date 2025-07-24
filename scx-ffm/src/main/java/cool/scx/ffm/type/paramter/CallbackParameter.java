package cool.scx.ffm.type.paramter;

import cool.scx.ffm.type.callback.Callback;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.ScxReflect;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;

import static cool.scx.ffm.FFMHelper.getMemoryLayout;
import static cool.scx.ffm.FFMHelper.getMemoryLayouts;
import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.invoke.MethodHandles.lookup;

/// CallbackParameter
///
/// @author scx567888
/// @version 0.0.1
public class CallbackParameter implements Parameter {

    private final Callback callback;
    private final MethodHandle fun;
    private final FunctionDescriptor functionDescriptor;

    public CallbackParameter(Callback callback) throws NoSuchMethodException, IllegalAccessException {
        this.callback = callback;
        var callbackMethodName = callback.callbackMethodName();
        var classInfo =(ClassInfo) ScxReflect.typeOf(callback.getClass());
        var methodInfo = Arrays.stream(classInfo.methods())
                .filter(method -> method.name().equals(callbackMethodName))
                .findFirst().orElseThrow(() -> new NoSuchMethodException(callbackMethodName));
        methodInfo.setAccessible(true);// 有时我们会遇到 callback 是一个 lambda 表达式的情况 这时需要 强制设置访问权限
        var method = methodInfo.rawMethod();
        this.fun = lookup().unreflect(method).bindTo(callback);
        var r = getMemoryLayout(method.getReturnType());
        var p = getMemoryLayouts(method.getParameterTypes());
        this.functionDescriptor = FunctionDescriptor.of(r, p);
    }

    @Override
    public Object toNativeParameter(Arena arena) {
        return nativeLinker().upcallStub(fun, functionDescriptor, arena);
    }

}

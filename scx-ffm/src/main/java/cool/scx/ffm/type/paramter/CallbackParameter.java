package cool.scx.ffm.type.paramter;

import cool.scx.ffm.type.callback.Callback;
import cool.scx.reflect.ReflectHelper;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;

import static cool.scx.ffm.FFMHelper.getMemoryLayout;
import static cool.scx.ffm.FFMHelper.getMemoryLayouts;
import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.invoke.MethodHandles.lookup;

/**
 * todo 参数校验不是特别完善
 */
public class CallbackParameter implements Parameter {

    private final Callback callback;
    private final MethodHandle fun;
    private final FunctionDescriptor functionDescriptor;

    public CallbackParameter(Callback callback) throws NoSuchMethodException, IllegalAccessException {
        this.callback = callback;
        var cc = callback.getClass().getInterfaces()[0];
        var s = ReflectHelper.getClassInfo(cc);
        var methodInfo = Arrays.stream(s.methods())
                .filter(method -> callback.callbackMethodName().equals(method.name()))
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("callback"));
        var method = methodInfo.method();
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

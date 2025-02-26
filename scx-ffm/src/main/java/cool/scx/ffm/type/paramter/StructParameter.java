package cool.scx.ffm.type.paramter;

import cool.scx.ffm.type.struct.Struct;
import cool.scx.reflect.AccessModifier;
import cool.scx.reflect.IFieldInfo;
import cool.scx.reflect.ReflectHelper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.StructLayout;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.ffm.FFMHelper.getMemoryLayout;

/**
 * StructParameter
 * todo 目前 只支持单层的 结构 需要支持多层
 *
 * @author scx567888
 * @version 0.0.1
 */
public class StructParameter implements Parameter {

    private final Object value;
    private final Map<IFieldInfo, VarHandle> fieldMap;
    private final StructLayout LAYOUT;
    private MemorySegment memorySegment;

    public StructParameter(Struct value) {
        this.value = value;
        var classInfo = ReflectHelper.getClassInfo(this.value.getClass());
        this.fieldMap = new HashMap<>();
        //1, 寻找 public 的 fields
        var field = Arrays.stream(classInfo.fields()).filter(c -> c.accessModifier() == AccessModifier.PUBLIC).toList();

        //2, 创建每个 field 对应的 内存布局
        var memoryLayouts = new MemoryLayout[field.size()];
        for (int i = 0; i < field.size(); i = i + 1) {
            var f = field.get(i);
            var memoryLayout = getMemoryLayout(f.type().getRawClass());
            memoryLayouts[i] = memoryLayout.withName(f.name());
        }

        this.LAYOUT = MemoryLayout.structLayout(memoryLayouts);

        //3, 创建 varHandle 以便能反向将 内存中的值读取出来
        for (var f : field) {
            var x = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement(f.name()));
            var ff = MethodHandles.insertCoordinates(x, 1, 0L);
            fieldMap.put(f, ff);
        }
    }

    @Override
    public Object toNativeParameter(Arena arena) {
        return this.memorySegment = arena.allocate(LAYOUT);
    }

    @Override
    public void beforeCloseArena() {
        for (var e : fieldMap.entrySet()) {
            var k = e.getKey();
            var v = e.getValue();
            var o = v.get(memorySegment);
            try {
                k.set(this.value, o);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}

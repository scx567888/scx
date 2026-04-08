package cool.scx.ffm.mapper;

import cool.scx.ffm.type.Struct;
import cool.scx.reflect.AccessModifier;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.FieldInfo;
import cool.scx.reflect.ScxReflect;

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

/// StructParameter
/// todo 目前 只支持单层的 结构 需要支持多层
///
/// @author scx567888
/// @version 0.0.1
public class StructMapper implements Mapper {

    private final Object value;
    private final Map<FieldInfo, VarHandle> fieldMap;
    private final StructLayout LAYOUT;

    public StructMapper(Struct value) {
        this.value = value;
        var classInfo = (ClassInfo) ScxReflect.typeOf(this.value.getClass());
        this.fieldMap = new HashMap<>();
        //1, 寻找 public 的 fields
        var fields = Arrays.stream(classInfo.allFields()).filter(c -> c.accessModifier() == AccessModifier.PUBLIC && !c.isStatic()).toList();

        //2, 创建每个 field 对应的 内存布局
        var memoryLayouts = new MemoryLayout[fields.size()];
        for (int i = 0; i < fields.size(); i = i + 1) {
            var f = fields.get(i);
            var memoryLayout = getMemoryLayout(f.rawField().getType());
            memoryLayouts[i] = memoryLayout.withName(f.name());
        }

        this.LAYOUT = MemoryLayout.structLayout(memoryLayouts);

        //3, 创建 varHandle 以便能反向将 内存中的值读取出来
        for (var f : fields) {
            var x = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement(f.name()));
            var ff = MethodHandles.insertCoordinates(x, 1, 0L);
            fieldMap.put(f, ff);
        }
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocate(LAYOUT);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
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

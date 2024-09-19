package cool.scx.common.ffm.type;

import cool.scx.common.reflect.AccessModifier;
import cool.scx.common.reflect.ClassInfo;
import cool.scx.common.reflect.FieldInfo;
import cool.scx.common.reflect.ReflectFactory;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.common.ffm.FFMHelper.getMemoryLayout;

/**
 * 只支持单层的 结构
 */
public class StructRef implements Ref {

    private final Object value;
    private final ClassInfo classInfo;
    private final Map<FieldInfo, VarHandle> fieldMap;
    private MemorySegment memorySegment;

    public StructRef(Object value) {
        this.value = value;
        this.classInfo = ReflectFactory.getClassInfo(this.value.getClass());
        this.fieldMap = new HashMap<>();
    }

    @Override
    public MemorySegment init(Arena arena) {
        var field = Arrays.stream(classInfo.fields()).filter(c -> c.accessModifier() == AccessModifier.PUBLIC).toList();

        var memoryLayouts = new MemoryLayout[field.size()];
        for (int i = 0; i < field.size(); i = i + 1) {
            var f = field.get(i);
            var memoryLayout = getMemoryLayout(f.type().getRawClass());
            memoryLayouts[i] = memoryLayout.withName(f.name());
        }

        var LAYOUT = MemoryLayout.structLayout(memoryLayouts);

        for (var f : field) {
            var x = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement(f.name()));
            var ff = MethodHandles.insertCoordinates(x, 1, 0L);
            fieldMap.put(f, ff);
        }

        return this.memorySegment = arena.allocate(LAYOUT);
    }

    @Override
    public void refresh() {
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

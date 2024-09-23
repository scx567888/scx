package cool.scx.common.ffm.type.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

public class FloatArrayMapper implements Mapper {

    private float[] value;

    public FloatArrayMapper(float[] value) {
        this.value = value;
    }

    public float[] getValue() {
        return value;
    }

    public void setValue(float[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_FLOAT, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        value = memorySegment.toArray(JAVA_FLOAT);
    }

}

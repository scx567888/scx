package cool.scx.ffm.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

/// FloatMapper
///
/// @author scx567888
/// @version 0.0.1
public class FloatMapper implements Mapper {

    private float value;

    public FloatMapper() {
        this.value = 0;
    }

    public FloatMapper(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_FLOAT, this.value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        this.value = memorySegment.get(JAVA_FLOAT, 0);
    }

}

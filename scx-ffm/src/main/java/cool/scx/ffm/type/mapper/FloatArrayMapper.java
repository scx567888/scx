package cool.scx.ffm.type.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

/**
 * FloatArrayMapper
 *
 * @author scx567888
 * @version 0.0.1
 */
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
    public Object fromMemorySegment(MemorySegment memorySegment) {
        value = memorySegment.toArray(JAVA_FLOAT);
        return value;
    }

}

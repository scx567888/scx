package cool.scx.common.ffm.type.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_DOUBLE;

public class DoubleArrayMapper implements Mapper {

    private double[] value;

    public DoubleArrayMapper(double[] value) {
        this.value = value;
    }

    public double[] getValue() {
        return value;
    }

    public void setValue(double[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_DOUBLE, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        value = memorySegment.toArray(JAVA_DOUBLE);
    }

}

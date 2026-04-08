package cool.scx.ffm.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_DOUBLE;

/// DoubleArrayMapper
///
/// @author scx567888
/// @version 0.0.1
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
        var temp = memorySegment.toArray(JAVA_DOUBLE);
        // 原因参考 IntArrayMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}

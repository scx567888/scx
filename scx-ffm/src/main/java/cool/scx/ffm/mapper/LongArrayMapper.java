package cool.scx.ffm.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_LONG;

/// LongArrayMapper
///
/// @author scx567888
/// @version 0.0.1
public class LongArrayMapper implements Mapper {

    private long[] value;

    public LongArrayMapper(long[] value) {
        this.value = value;
    }

    public long[] getValue() {
        return value;
    }

    public void setValue(long[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_LONG, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_LONG);
        // 原因参考 IntArrayMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}

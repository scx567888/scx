package cool.scx.ffm.type.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_LONG;

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
    public Object fromMemorySegment(MemorySegment memorySegment) {
        value = memorySegment.toArray(JAVA_LONG);
        return value;
    }

}

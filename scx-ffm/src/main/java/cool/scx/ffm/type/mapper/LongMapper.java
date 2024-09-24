package cool.scx.ffm.type.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_LONG;

public class LongMapper implements Mapper {

    private long value;

    public LongMapper() {
        this.value = 0;
    }

    public LongMapper(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_LONG, this.value);
    }

    @Override
    public Object fromMemorySegment(MemorySegment memorySegment) {
        this.value = memorySegment.get(JAVA_LONG, 0);
        return value;
    }

}

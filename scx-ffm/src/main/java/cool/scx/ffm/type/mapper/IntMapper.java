package cool.scx.ffm.type.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_INT;

public class IntMapper implements Mapper {

    private int value;

    public IntMapper() {
        this.value = 0;
    }

    public IntMapper(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_INT, this.value);
    }

    @Override
    public Object fromMemorySegment(MemorySegment memorySegment) {
        this.value = memorySegment.get(JAVA_INT, 0);
        return value;
    }

}

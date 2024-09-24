package cool.scx.ffm.type.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_CHAR;

public class CharArrayMapper implements Mapper {

    private char[] value;

    public CharArrayMapper(char[] value) {
        this.value = value;
    }

    public char[] getValue() {
        return value;
    }

    public void setValue(char[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_CHAR, value);
    }

    @Override
    public Object fromMemorySegment(MemorySegment memorySegment) {
        value = memorySegment.toArray(JAVA_CHAR);
        return value;
    }

}

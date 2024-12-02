package cool.scx.ffm.type.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_SHORT;

/**
 * ShortArrayMapper
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ShortArrayMapper implements Mapper {

    private short[] value;

    public ShortArrayMapper(short[] value) {
        this.value = value;
    }

    public short[] getValue() {
        return value;
    }

    public void setValue(short[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_SHORT, value);
    }

    @Override
    public Object fromMemorySegment(MemorySegment memorySegment) {
        value = memorySegment.toArray(JAVA_SHORT);
        return value;
    }

}

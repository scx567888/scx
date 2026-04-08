package cool.scx.ffm.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_CHAR;

/// CharArrayMapper
///
/// @author scx567888
/// @version 0.0.1
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
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_CHAR);
        // 原因参考 IntArrayMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}

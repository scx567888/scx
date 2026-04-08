package cool.scx.ffm.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;

/// ByteArrayMapper
///
/// @author scx567888
/// @version 0.0.1
public class ByteArrayMapper implements Mapper {

    private byte[] value;

    public ByteArrayMapper(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_BYTE, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_BYTE);
        // 原因参考 IntArrayMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}

package cool.scx.common.ffm.type;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_CHAR;

public class CharArrayRef implements Ref {

    private final char[] value;
    private MemorySegment memorySegment;

    public CharArrayRef(char[] value) {
        this.value = value;
    }

    @Override
    public MemorySegment init(Arena arena) {
        return this.memorySegment = arena.allocateArray(JAVA_CHAR, value);
    }

    @Override
    public void refresh() {
        var chars = this.memorySegment.toArray(JAVA_CHAR);
        System.arraycopy(chars, 0, value, 0, value.length);
    }

}

package cool.scx.common.ffm.type;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_INT;

public class IntRef implements Ref {


    private int value;
    private MemorySegment memorySegment;

    public IntRef() {
        this(0);
    }

    public IntRef(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public MemorySegment init(Arena arena) {
        return this.memorySegment = arena.allocate(JAVA_INT, this.value);
    }

    @Override
    public void refresh() {
        this.value = this.memorySegment.get(JAVA_INT, 0);
    }

}

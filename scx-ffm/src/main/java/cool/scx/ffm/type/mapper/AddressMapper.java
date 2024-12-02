package cool.scx.ffm.type.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/**
 * AddressMapper
 *
 * @author scx567888
 * @version 0.0.1
 */
public class AddressMapper implements Mapper {

    private MemorySegment value;

    public AddressMapper(MemorySegment value) {
        this.value = value;
    }

    public MemorySegment getValue() {
        return value;
    }

    public void setValue(MemorySegment value) {
        this.value = value;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return value;
    }

    @Override
    public Object fromMemorySegment(MemorySegment memorySegment) {
        this.value = memorySegment;
        return value;
    }

}

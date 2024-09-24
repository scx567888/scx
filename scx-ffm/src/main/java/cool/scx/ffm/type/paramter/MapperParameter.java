package cool.scx.ffm.type.paramter;

import cool.scx.ffm.type.mapper.Mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public class MapperParameter implements Parameter {

    private final Mapper mapper;

    private MemorySegment memorySegment;

    public MapperParameter(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object toNativeParameter(Arena arena) {
        return memorySegment = mapper.toMemorySegment(arena);
    }

    @Override
    public void beforeCloseArena() {
        mapper.fromMemorySegment(memorySegment);
    }

}
